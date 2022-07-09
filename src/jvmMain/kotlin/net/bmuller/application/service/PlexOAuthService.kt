package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import entities.LoginUrlResponse
import io.ktor.http.*
import io.ktor.server.util.*
import net.bmuller.application.entities.PlexClientHeaders
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.MissingOrInvalidParam
import net.bmuller.application.repository.PlexAuthPinRepository

interface IPlexOAuthService {
	suspend fun requestHostedLoginURL(clientInfo: PlexClientDetails): Either<DomainError, LoginUrlResponse>

	suspend fun checkForAuthToken(
		pinId: Long?, clientInfo: PlexClientHeaders = PlexClientHeaders()
	): Either<DomainError, String>
}

data class PlexClientDetails(
	val headers: PlexClientHeaders = PlexClientHeaders(),
	val forwardUrl: String,
)

/**
 * For reference: https://forums.plex.tv/t/authenticating-with-plex/609370
 */
fun plexOAuthService(plexAuthPinRepository: PlexAuthPinRepository) = object : IPlexOAuthService {
	private val PLEX_AUTH_HOST = "app.plex.tv"
	private val PLEX_AUTH_PATH = "auth"
	private val plexApiUrl = url {
		protocol = URLProtocol.HTTPS
		host = PLEX_AUTH_HOST
		path(PLEX_AUTH_PATH)
	}

	override suspend fun requestHostedLoginURL(clientInfo: PlexClientDetails): Either<DomainError, LoginUrlResponse> =
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

			LoginUrlResponse(loginUrl, pin.id)
		}

	override suspend fun checkForAuthToken(
		pinId: Long?, clientInfo: PlexClientHeaders
	): Either<DomainError, String> {
		return pinId?.let { pin -> plexAuthPinRepository.pollForAuthToken(pin, clientInfo) }
			?: MissingOrInvalidParam("pinId is missing or not of type Long").left()
	}
}

