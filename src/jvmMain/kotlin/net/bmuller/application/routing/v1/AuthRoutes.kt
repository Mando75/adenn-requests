package net.bmuller.application.routing.v1

import arrow.core.Either
import arrow.core.continuations.either
import http.AuthResource
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.*
import net.bmuller.application.service.IPlexOAuthService
import net.bmuller.application.service.IUserAuthService
import net.bmuller.application.service.PlexClientDetails


fun Route.auth(plexOAuthService: IPlexOAuthService, userAuthService: IUserAuthService) {

	get<AuthResource.Plex.LoginUrl> { context ->
		either<DomainError, Unit> {
			val clientDetails =
				PlexClientDetails(forwardUrl = "${context.forwardHost}/api/v1/auth/plex/callback")
			plexOAuthService.requestHostedLoginURL(clientDetails).bind()
		}.respond()
	}

	get<AuthResource.Plex.Callback> { resource ->
		either {
			val pinId = resource.pinId.toLongOrNull()
			val authToken = plexOAuthService.checkForAuthToken(pinId).bind()
			val user = userAuthService.signInFlow(authToken).bind()

			call.sessions.set(UserSession(user.id, user.plexUsername, user.authVersion))
		}.respondRedirect("/?login=success")
	}

	get<AuthResource.Logout> {
		Either.catchUnknown {
			call.sessions.clear<UserSession>()
		}.respondRedirect("/")
	}

	authenticate("user_session") {
		post<AuthResource.Token> {
			either<DomainError, Unit> {
				val user = principalCatching<UserSession>().bind()
				userAuthService.createJwtToken(user)
			}.respond()
		}
	}

}