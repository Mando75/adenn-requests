package net.bmuller.application.repository

import arrow.core.Either
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.resources.*
import kotlinx.coroutines.delay
import net.bmuller.application.http.plex.PlexCodeResponse

interface PlexAuthPinRepository {
	private val DEFAULT_MAX_RETRIES: Int
		get() = 20

	suspend fun getPin(): Either<Throwable, PlexCodeResponse>
	suspend fun pollForAuthToken(pinId: Long, retries: Int = DEFAULT_MAX_RETRIES): Either<Throwable, String?>
}

class PlexAuthPinRepositoryImpl : BaseRepository(), PlexAuthPinRepository {

	private val POLL_DELAY = 1500L

	@Resource("/")
	@kotlinx.serialization.Serializable
	class AuthPinResources {
		@Resource("pins")
		@kotlinx.serialization.Serializable
		class Pins(val parent: AuthPinResources = AuthPinResources()) {

			@Resource("{id}")
			@kotlinx.serialization.Serializable
			class ID(val parent: Pins = Pins(), val id: Long)
		}
	}

	override suspend fun getPin(): Either<Throwable, PlexCodeResponse> = Either.catch {
		val response = plex.pinClient.post(AuthPinResources.Pins()) {
			parameter("strong", true)
		}
		return@catch response.body<PlexCodeResponse>()
	}

	override suspend fun pollForAuthToken(pinId: Long, retries: Int): Either<Throwable, String?> =
		Either.catch {
			repeat(retries) {
				val response = plex.pinClient.get(AuthPinResources.Pins.ID(id = pinId))
				val pin = response.body<PlexCodeResponse>()
				if (pin.authToken !== null) {
					return@catch pin.authToken
				}
				delay(POLL_DELAY)
			}
			return@catch null
		}
}