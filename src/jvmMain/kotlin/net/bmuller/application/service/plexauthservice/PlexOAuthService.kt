package net.bmuller.application.service.plexauthservice

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import io.ktor.http.*
import net.bmuller.application.http.plex.PlexClientHeaders

class PlexOAuthService : BaseService() {

	private val PLEX_AUTH_HOST = "app.plex.tv"
	private val PLEX_AUTH_PATH = "/auth"

	data class PlexClientDetails(
		val headers: PlexClientHeaders = PlexClientHeaders(),
		val forwardUrl: String,
	)

	@kotlinx.serialization.Serializable
	data class LoginUrlResponse(val loginUrl: String, val pinId: Long)

	suspend fun requestHostedLoginURL(clientInfo: PlexClientDetails): Either<Throwable, LoginUrlResponse> =
		either {
			val pin = plexAuthPinRepository.getPin(clientInfo.headers).bind()
			val forwardUrl = Url(clientInfo.forwardUrl + "?pinId=${pin.id}").toString()

			val loginUrl =
				"https://$PLEX_AUTH_HOST/$PLEX_AUTH_PATH/#!?code=${pin.code}" +
						"&context[device][product]=${clientInfo.headers.product}" +
						"&context[device][device]=${clientInfo.headers.device}" +
						"&clientID=${pin.clientIdentifier}" + "&forwardUrl=$forwardUrl"


			return@either LoginUrlResponse(loginUrl, pin.id)
		}

	sealed class CheckForAuthTokenError {
		object MissingPinId : CheckForAuthTokenError()
		data class Unknown(val message: String?) : CheckForAuthTokenError()
	}

	suspend fun checkForAuthToken(
		pinId: Long?, clientInfo: PlexClientHeaders = PlexClientHeaders()
	): Either<CheckForAuthTokenError, String?> {
		if (pinId == null || pinId == 0L) {
			return CheckForAuthTokenError.MissingPinId.left()
		}
		return plexAuthPinRepository.pollForAuthToken(pinId, clientInfo)
			.mapLeft { error -> CheckForAuthTokenError.Unknown(error.message) }
	}

}
