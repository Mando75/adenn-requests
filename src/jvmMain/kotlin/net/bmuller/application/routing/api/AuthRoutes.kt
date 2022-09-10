package net.bmuller.application.routing.api

import arrow.core.Either
import arrow.core.continuations.either
import http.AuthResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.catchUnknown
import net.bmuller.application.lib.principalCatching
import net.bmuller.application.lib.respond
import net.bmuller.application.lib.respondRedirect
import net.bmuller.application.service.PlexClientDetails
import net.bmuller.application.service.PlexOAuthService
import net.bmuller.application.service.UserAuthService


fun Route.auth(plexOAuthService: PlexOAuthService, userAuthService: UserAuthService) {

	get<AuthResource.Plex.LoginUrl> { context ->
		either {
			val clientDetails =
				PlexClientDetails(forwardUrl = "${context.forwardHost}/api/auth/plex/callback")
			plexOAuthService.requestHostedLoginURL(clientDetails).bind()
		}.respond()
	}

	get<AuthResource.Plex.Callback> { resource ->
		either {
			val pinId = resource.pinId.toLongOrNull()
			val authToken = plexOAuthService.checkForAuthToken(pinId).bind()
			val user = userAuthService.signInFlow(authToken).bind()

			call.sessions.set(UserSession(user.id, user.plexUsername, user.authVersion, user.userType))
		}.respondRedirect("/")
	}

	get<AuthResource.Logout> {
		Either.catchUnknown {
			call.sessions.clear<UserSession>()
		}.respondRedirect("/")
	}

	get<AuthResource.Initialize.LoginUrl> { context ->
		either {
			userAuthService.canInitialize().bind()
			val clientDetails = PlexClientDetails(forwardUrl = "${context.forwardHost}/api/auth/init/callback")
			plexOAuthService.requestHostedLoginURL(clientDetails).bind()
		}.respond()
	}

	get<AuthResource.Initialize.Callback> { context ->
		either {
			userAuthService.canInitialize().bind()
			val pinId = context.pinId.toLongOrNull()
			val authToken = plexOAuthService.checkForAuthToken(pinId).bind()
			val user = userAuthService.initializeFlow(authToken).bind()

			call.sessions.set(UserSession(user.id, user.plexUsername, user.authVersion, user.userType))
		}.respondRedirect("/")
	}

	authenticate("user_session") {
		post<AuthResource.Token> {
			either {
				val user = principalCatching<UserSession>().bind()
				userAuthService.createJwtToken(user).bind()
			}.respond(HttpStatusCode.Created)
		}
	}

}