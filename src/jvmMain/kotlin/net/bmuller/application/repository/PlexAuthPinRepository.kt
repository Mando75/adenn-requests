package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import kotlinx.coroutines.delay
import net.bmuller.application.entities.PlexClientHeaders
import net.bmuller.application.entities.PlexCodeResponse

sealed class PollForAuthTokenError {
	data class Unknown(val message: String?) : PollForAuthTokenError()
	object TimedOut : PollForAuthTokenError()
}

interface PlexAuthPinRepository {

	suspend fun getPin(clientHeaders: PlexClientHeaders): Either<Throwable, PlexCodeResponse>
	suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int = DEFAULT_MAX_RETRIES
	): Either<PollForAuthTokenError, String>

	companion object {
		private const val DEFAULT_MAX_RETRIES = 60
	}
}

@Suppress("unused")
@Resource("/api")
@kotlinx.serialization.Serializable
class AuthPinResources {

	@Resource("v2")
	@kotlinx.serialization.Serializable
	class V2(val parent: AuthPinResources = AuthPinResources()) {
		@Resource("pins")
		@kotlinx.serialization.Serializable
		class Pins(val parent: V2 = V2()) {

			@Resource("{id}")
			@kotlinx.serialization.Serializable
			class ID(val parent: Pins = Pins(), val id: Long)
		}
	}
}

class PlexAuthPinRepositoryImpl : BaseRepository(), PlexAuthPinRepository {

	companion object {
		private const val POLL_DELAY = 1500L
	}

	override suspend fun getPin(clientHeaders: PlexClientHeaders): Either<Throwable, PlexCodeResponse> = Either.catch {
		val response = plex.client.post(AuthPinResources.V2.Pins()) {
			parameter("strong", true)
			headers(buildHeaders(clientHeaders))
		}
		return@catch response.body<PlexCodeResponse>()
	}

	override suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int
	): Either<PollForAuthTokenError, String> {
		repeat(retries) {
			try {
				val response = plex.client.get(AuthPinResources.V2.Pins.ID(id = pinId)) {
					headers(buildHeaders(clientHeaders))
				}
				val pin = response.body<PlexCodeResponse>()
				if (pin.authToken !== null) {
					return pin.authToken.right()
				}
				delay(POLL_DELAY)
			} catch (e: Throwable) {
				return PollForAuthTokenError.Unknown(e.message).left()
			}
		}
		return PollForAuthTokenError.TimedOut.left()
	}

	private fun buildHeaders(clientHeaders: PlexClientHeaders): HeadersBuilder.() -> Unit {
		return {
			append("X-Plex-Client-Identifier", clientHeaders.clientId)
			append("X-Plex-Device", clientHeaders.device)
			append("X-Plex-Platform", clientHeaders.platform)
			append("X-Plex-Product", clientHeaders.product)
			append("X-Plex-version", clientHeaders.version)
		}
	}

}