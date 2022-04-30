package net.bmuller.application.repository

import arrow.core.Either
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import kotlinx.coroutines.delay
import net.bmuller.application.http.plex.PlexClientHeaders
import net.bmuller.application.http.plex.PlexCodeResponse

interface PlexAuthPinRepository {
	private val DEFAULT_MAX_RETRIES: Int
		get() = 60

	suspend fun getPin(clientHeaders: PlexClientHeaders): Either<Throwable, PlexCodeResponse>
	suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int = DEFAULT_MAX_RETRIES
	): Either<Throwable, String?>
}

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

	private val POLL_DELAY = 1500L

	override suspend fun getPin(clientHeaders: PlexClientHeaders): Either<Throwable, PlexCodeResponse> = Either.catch {
		val response = plex.pinClient.post(AuthPinResources.V2.Pins()) {
			parameter("strong", true)
			headers(buildHeaders(clientHeaders))
		}
		return@catch response.body<PlexCodeResponse>()
	}

	override suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int
	): Either<Throwable, String?> =
		Either.catch {
			repeat(retries) {
				val response = plex.pinClient.get(AuthPinResources.V2.Pins.ID(id = pinId)) {
					headers(buildHeaders(clientHeaders))
				}
				val pin = response.body<PlexCodeResponse>()
				if (pin.authToken !== null) {
					return@catch pin.authToken
				}
				delay(POLL_DELAY)
			}
			return@catch null
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