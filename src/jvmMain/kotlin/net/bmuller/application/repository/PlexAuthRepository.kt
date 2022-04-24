package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.computations.either
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import net.bmuller.application.http.plex.PlexClient
import net.bmuller.application.http.plex.PlexClientError
import net.bmuller.application.http.plex.PlexCodeResponse
import org.koin.java.KoinJavaComponent
import java.net.URL

class PlexAuthRepository : BaseRepository() {

	class AuthPin {
		private val PIN_BASE_URL = "https://plex.tv/api/v2"
		private val POLL_DELAY = 1500L
		private val MAX_RETRIES = 20
		private val plexClient: PlexClient by KoinJavaComponent.inject(PlexClient::class.java)

		public suspend fun getPin(): Either<PlexClientError, PlexCodeResponse> = either {
			val request = HttpRequestBuilder(url = URL("$PIN_BASE_URL/pins"))
			request.method = HttpMethod.Post
			request.parameter("strong", true)
			val (code) = plexClient.exec<PlexCodeResponse>(request).bind()
			return@either code
		}

		public suspend fun pollForAuthToken(pinId: Int, retries: Int = MAX_RETRIES): Either<PlexClientError, String?> =
			either {
				val request = HttpRequestBuilder(url = URL("$PIN_BASE_URL/pins/$pinId"))
				request.method = HttpMethod.Get
				repeat(retries) {
					val (pin) = plexClient.exec<PlexCodeResponse>(request).bind()
					if (pin.authToken !== null) {
						return@either pin.authToken
					}
					delay(POLL_DELAY)
				}
				return@either null
			}
	}
}