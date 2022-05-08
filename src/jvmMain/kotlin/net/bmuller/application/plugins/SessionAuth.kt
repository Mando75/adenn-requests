package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.entities.UserSession
import net.bmuller.application.service.UserAuthService


fun Application.configureSessionAuth() {
	val env: EnvironmentValues by inject()
	val userAuthentication: UserAuthService by inject()

	install(Authentication) {
		session<UserSession> {
			validate { session ->
				val validAuthToken =
					userAuthentication.validateAuthToken(session.id)
				return@validate if (validAuthToken) session else null
			}
			challenge {
				call.respond(HttpStatusCode.Unauthorized, "Not Authorized")
			}
		}
	}
	install(Sessions) {
		val secretEncryptKey = hex(env.sessionSecretEncryptKey)
		val secretSignKey = hex(env.sessionSignKey)
		cookie<UserSession>("ar.sid") {
			cookie.path = "/"
			cookie.httpOnly = true
			cookie.maxAgeInSeconds = 60 * 60 * 24 * 7
			cookie.secure = env.prod
			cookie.extensions["SameSite"] = "Strict"
			transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
		}
	}
}
