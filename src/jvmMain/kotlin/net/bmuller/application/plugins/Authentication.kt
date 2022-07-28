package net.bmuller.application.plugins

import arrow.core.Either
import arrow.core.rightIfNotNull
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
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.GenericErrorModel
import net.bmuller.application.lib.Unauthorized
import net.bmuller.application.lib.catchUnknown
import net.bmuller.application.service.UserAuthService


fun Application.configureAuthentication(env: Env, userAuthService: UserAuthService) {

	install(Authentication) {
		session<UserSession>("user_session") {
			validate { session ->
				when (userAuthService.validateAuthToken(session.id, session.version)) {
					is Either.Left -> null
					is Either.Right -> session
				}
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
					when (userAuthService.validateAuthToken(userId, version)) {
						is Either.Left -> null
						is Either.Right -> JWTPrincipal(credential.payload)
					}
				}
			}
			challenge { challenge, realm ->
				call.respond(
					HttpStatusCode.Unauthorized,
					GenericErrorModel("Token is not valid or has expired for $realm", challenge)
				)
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

fun ApplicationCall.parseUserAuth(): Either<DomainError, UserSession> = Either.catchUnknown {
	val principal = principal() ?: principal<JWTPrincipal>()?.let { jwt ->
		val id = jwt.getClaim("userId", Int::class)!!
		val plexUsername = jwt.getClaim("plexUsername", String::class)!!
		val version = jwt.getClaim("version", Int::class)!!
		return@let UserSession(id, plexUsername, version)
	}

	return principal.rightIfNotNull { Unauthorized }
}