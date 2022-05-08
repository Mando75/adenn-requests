package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import net.bmuller.application.entities.UserSession
import net.bmuller.application.service.PlexOAuthService


fun Application.configureSessionAuth() {
	install(Authentication) {
		session<UserSession> {
			validate { session ->
				val plexAuthService: PlexOAuthService by inject()
				val validAuthToken = plexAuthService.validateAuthToken(session.id, session.plexUsername)
				return@validate if (validAuthToken) session else null
			}
			challenge {
				call.respond(HttpStatusCode.Unauthorized, "Not Authorized")
			}
		}
	}
	install(Sessions) {
		cookie<UserSession>("ar.sid") {
			cookie.path = "/"
			cookie.httpOnly = true
			cookie.maxAgeInSeconds = 60 * 60 * 24 * 7
		}
	}
}
