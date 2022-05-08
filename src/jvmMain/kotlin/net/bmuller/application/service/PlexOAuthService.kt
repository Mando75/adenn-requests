package net.bmuller.application.service

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import io.ktor.http.*
import io.ktor.server.util.*
import net.bmuller.application.entities.PlexClientHeaders
import net.bmuller.application.repository.PollForAuthTokenError

/**
 * Class for implementing Plex OAuth Flow
 * For reference: https://forums.plex.tv/t/authenticating-with-plex/609370
 */
class PlexOAuthService : BaseService() {

	companion object {
		private const val PLEX_AUTH_HOST = "app.plex.tv"
		private const val PLEX_AUTH_PATH = "auth"
		private val plexApiUrl = url {
			protocol = URLProtocol.HTTPS
			host = PLEX_AUTH_HOST
			path(PLEX_AUTH_PATH)
		}
	}

	data class PlexClientDetails(
		val headers: PlexClientHeaders = PlexClientHeaders(),
		val forwardUrl: String,
	)

	@kotlinx.serialization.Serializable
	data class LoginUrlResponse(val loginUrl: String, val pinId: Long)

	suspend fun requestHostedLoginURL(clientInfo: PlexClientDetails): Either<Throwable, LoginUrlResponse> =
		either {
			val pin = plexAuthPinRepository.getPin(clientInfo.headers).bind()

			val forwardUrlParams: Parameters = Parameters.build {
				append("pinId", pin.id.toString())
			}

			val forwardUrl = Url("${clientInfo.forwardUrl}?${forwardUrlParams.formUrlEncode()}").toString()


			val loginParameters: Parameters = Parameters.build {
				append("code", pin.code)
				append("context[device][product]", clientInfo.headers.product)
				append("context[device][device]", clientInfo.headers.device)
				append("clientID", pin.clientIdentifier)
				append("forwardUrl", forwardUrl)
			}
			// Wanted to use the URL constructor for this, but I couldn't find a
			// way to add the anchor tag without it being encoded.
			// Plex uses the anchor tag to indicate it should parse the url params
			val loginUrl = "$plexApiUrl#!?${loginParameters.formUrlEncode()}"


			return@either LoginUrlResponse(loginUrl, pin.id)
		}

	sealed class CheckForAuthTokenError {
		object TimedOutWaitingForToken : CheckForAuthTokenError()
		object MissingPinId : CheckForAuthTokenError()
		data class Unknown(val message: String?) : CheckForAuthTokenError()
	}

	suspend fun checkForAuthToken(
		pinId: Long?, clientInfo: PlexClientHeaders = PlexClientHeaders()
	): Either<CheckForAuthTokenError, String> {
		if (pinId == null || pinId == 0L) {
			return CheckForAuthTokenError.MissingPinId.left()
		}
		return plexAuthPinRepository.pollForAuthToken(pinId, clientInfo)
			.mapLeft { error ->
				when (error) {
					is PollForAuthTokenError.TimedOut -> CheckForAuthTokenError.TimedOutWaitingForToken
					is PollForAuthTokenError.Unknown -> CheckForAuthTokenError.Unknown(error.message)
				}
			}
	}

	suspend fun validateAuthToken(userId: Int, username: String, authToken: String): Boolean {
		return userRepository
			.getUserPlexToken(userId)
			?.let { token -> if (token == authToken) token else null }
			?.let { token ->
				try {
					val plexUser = plexTVRepository.getUser(token)
					token == authToken && plexUser.username == username
				} catch (e: Throwable) {
					false
				}
			} ?: false
	}
}
