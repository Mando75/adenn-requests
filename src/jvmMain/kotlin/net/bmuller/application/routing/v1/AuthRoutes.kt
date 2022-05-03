package net.bmuller.application.routing.v1

import arrow.core.flatMap
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.plugins.inject
import net.bmuller.application.service.PlexOAuthService
import net.bmuller.application.service.UserAuthService


@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/auth")
class AuthResource {
	@kotlinx.serialization.Serializable
	@Resource("plex")
	class Plex(val parent: AuthResource = AuthResource()) {

		@kotlinx.serialization.Serializable
		@Resource("login-url")
		class LoginUrl(val parent: Plex = Plex())

		@kotlinx.serialization.Serializable
		@Resource("callback")
		class Callback(val parent: Plex = Plex(), val pinId: String)
	}
}

fun Route.auth() {
	val plexOAuthService: PlexOAuthService by inject()
	val userAuthService: UserAuthService by inject()

	get<AuthResource.Plex.LoginUrl> {
		val clientDetails =
			PlexOAuthService.PlexClientDetails(forwardUrl = "http://localhost:8080/api/v1/auth/plex/callback")
		plexOAuthService.requestHostedLoginURL(clientDetails).mapLeft { error ->
			call.application.environment.log.error(error.message)
			call.respond(HttpStatusCode.InternalServerError, "An unknown error occurred")
		}.map { result ->
			call.respond(HttpStatusCode.OK, result)
		}
	}

	get<AuthResource.Plex.Callback> { resource ->
		val pinId = resource.pinId
		plexOAuthService.checkForAuthToken(pinId.toLongOrNull())
			.mapLeft { error ->
				when (error) {
					is PlexOAuthService.CheckForAuthTokenError.MissingPinId ->
						call.respond(HttpStatusCode.BadRequest, "Missing or invalid pinId")
					is PlexOAuthService.CheckForAuthTokenError.TimedOutWaitingForToken ->
						call.respond(HttpStatusCode.RequestTimeout, mapOf("msg" to "Timed out waiting for token"))
					is PlexOAuthService.CheckForAuthTokenError.Unknown -> {
						call.application.environment.log.error(error.message)
						call.respond(HttpStatusCode.InternalServerError, "An unknown error occurred")
					}
				}
			}
			.flatMap { token -> userAuthService.registerNewUser(token) }
			.map { user -> call.respond(HttpStatusCode.OK, user.value) }
			.mapLeft { error -> call.respond(HttpStatusCode.InternalServerError, error.toString()) }
	}
}