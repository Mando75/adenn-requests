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
import net.bmuller.application.http.PlexClient
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.TimedOut
import net.bmuller.application.lib.catchUnknown

interface PlexAuthPinRepository {
	suspend fun getPin(clientHeaders: PlexClientHeaders): Either<DomainError, PlexCodeResponse>
	suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int = DEFAULT_MAX_RETRIES
	): Either<DomainError, String>

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

fun plexAuthPinRepository(plex: PlexClient) = object : PlexAuthPinRepository {
	private val POLL_DELAY = 1500L

	override suspend fun getPin(clientHeaders: PlexClientHeaders): Either<DomainError, PlexCodeResponse> =
		Either.catchUnknown {
			val response = plex.client.post(AuthPinResources.V2.Pins()) {
				parameter("strong", true)
				headers(buildHeaders(clientHeaders))
			}
			response.body()
		}

	override suspend fun pollForAuthToken(
		pinId: Long,
		clientHeaders: PlexClientHeaders,
		retries: Int
	): Either<DomainError, String> {
		repeat(retries) {
			Either.catchUnknown {
				val response = plex.client.get(AuthPinResources.V2.Pins.ID(id = pinId)) {
					headers(buildHeaders(clientHeaders))
				}
				val pin = response.body<PlexCodeResponse>()
				if (pin.authToken !== null) {
					return pin.authToken.right()
				}
				delay(POLL_DELAY)
			}
		}
		return TimedOut("Timed out waiting for Plex token").left()
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