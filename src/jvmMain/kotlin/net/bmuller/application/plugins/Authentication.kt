package net.bmuller.application.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.entities.UserSession
import net.bmuller.application.service.UserAuthService


fun Application.configureAuthentication() {
	val env: EnvironmentValues by inject()
	val userAuthentication: UserAuthService by inject()

	install(Authentication) {
		session<UserSession>("user_session") {
			validate { session ->
				val validAuthToken =
					userAuthentication.validateAuthToken(session.id, session.version)
				return@validate if (validAuthToken) session else null
			}
			challenge {
				call.respond(HttpStatusCode.Unauthorized, "Not Authorized")
			}
		}
		jwt("bearer_token") {
			realm = env.jwtRealm
			verifier(
				JWT.require(Algorithm.HMAC256(env.jwtTokenSecret))
					.withAudience(env.jwtAudience)
					.withIssuer(env.jwtIssuer)
					.build()
			)
			validate { credential ->
				credential.payload.getClaim("plexUsername")?.let {
					val userId = credential.payload.getClaim("userId").asInt()
					val version = credential.payload.getClaim("version").asInt()
					val validToken = userAuthentication.validateAuthToken(userId, version)
					return@validate if (validToken) JWTPrincipal(credential.payload) else null
				}
			}
			challenge { _, realm ->
				call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired for $realm")
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

fun ApplicationCall.parseUserAuth(): UserSession? {
	return principal() ?: principal<JWTPrincipal>()?.let { jwt ->
		val id = jwt.getClaim("userId", Int::class)!!
		val plexUsername = jwt.getClaim("plexUsername", String::class)!!
		val version = jwt.getClaim("version", Int::class)!!
		return@let UserSession(id, plexUsername, version)
	}
}