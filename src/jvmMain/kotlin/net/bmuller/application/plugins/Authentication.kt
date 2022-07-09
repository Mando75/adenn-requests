package net.bmuller.application.plugins

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import net.bmuller.application.config.Env
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.error.DomainError
import net.bmuller.application.lib.error.Unauthorized
import net.bmuller.application.service.IUserAuthService


fun Application.configureAuthentication(env: Env, userAuthService: IUserAuthService) {

	install(Authentication) {
		session<UserSession>("user_session") {
			validate { session ->
				val validAuthToken =
					userAuthService.validateAuthToken(session.id, session.version)
				return@validate if (validAuthToken) session else null
			}
			challenge {
				call.respond(HttpStatusCode.Unauthorized, "Not Authorized")
			}
		}
		jwt("bearer_token") {
			realm = env.auth.jwtRealm
			verifier(
				JWT.require(Algorithm.HMAC256(env.auth.jwtTokenSecret))
					.withAudience(env.auth.jwtAudience)
					.withIssuer(env.auth.jwtIssuer)
					.build()
			)
			validate { credential ->
				credential.payload.getClaim("plexUsername")?.let {
					val userId = credential.payload.getClaim("userId").asInt()
					val version = credential.payload.getClaim("version").asInt()
					val validToken = userAuthService.validateAuthToken(userId, version)
					return@validate if (validToken) JWTPrincipal(credential.payload) else null
				}
			}
			challenge { _, realm ->
				call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired for $realm")
			}
		}
	}
	install(Sessions) {
		val secretEncryptKey = hex(env.auth.sessionSecretEncryptKey)
		val secretSignKey = hex(env.auth.sessionSignKey)
		cookie<UserSession>("ar.sid") {
			cookie.path = "/"
			cookie.httpOnly = true
			cookie.maxAgeInSeconds = 60 * 60 * 24 * 7
			cookie.secure = env.http.prod
			cookie.extensions["SameSite"] = "Strict"
			transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
		}
	}
}

fun ApplicationCall.parseUserAuth(): Either<DomainError, UserSession> {
	val principal = principal() ?: principal<JWTPrincipal>()?.let { jwt ->
		val id = jwt.getClaim("userId", Int::class)!!
		val plexUsername = jwt.getClaim("plexUsername", String::class)!!
		val version = jwt.getClaim("version", Int::class)!!
		return@let UserSession(id, plexUsername, version)
	}

	return principal?.right() ?: Unauthorized.left()
}