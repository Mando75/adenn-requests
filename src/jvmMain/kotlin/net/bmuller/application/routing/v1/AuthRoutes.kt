package net.bmuller.application.routing.v1

import arrow.core.computations.either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.entities.UserSession
import net.bmuller.application.plugins.inject
import net.bmuller.application.service.PlexOAuthService
import net.bmuller.application.service.UserAuthErrors
import net.bmuller.application.service.UserAuthService
import java.util.*


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

	@kotlinx.serialization.Serializable
	@Resource("logout")
	class Logout(val parent: AuthResource = AuthResource())

	@kotlinx.serialization.Serializable
	@Resource("token")
	class Token(val parent: AuthResource = AuthResource())
}

fun Route.auth() {
	val env: EnvironmentValues by inject()
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
		either<Any, Any> {
			val pinId = resource.pinId
			val authToken = plexOAuthService.checkForAuthToken(pinId.toLongOrNull())
				.mapLeft { error ->
					val (statusCode, message) = when (error) {
						is PlexOAuthService.CheckForAuthTokenError.MissingPinId ->
							Pair(HttpStatusCode.BadRequest, "Missing or invalid pinId")
						is PlexOAuthService.CheckForAuthTokenError.TimedOutWaitingForToken ->
							Pair(HttpStatusCode.RequestTimeout, mapOf("msg" to "Timed out waiting for token"))
						is PlexOAuthService.CheckForAuthTokenError.Unknown -> {
							call.application.environment.log.error(error.message)
							Pair(HttpStatusCode.InternalServerError, "An unknown error occurred")
						}
					}
					call.respond(statusCode, message)
				}.bind()
			val user = userAuthService.signInFlow(authToken)
				.mapLeft { error ->
					val (statusCode, message) = when (error) {
						is UserAuthErrors.CouldNotFetchPlexUser -> Pair(
							HttpStatusCode.InternalServerError,
							"Error fetching Plex user data"
						)
						is UserAuthErrors.ErrorFetchingUser -> Pair(
							HttpStatusCode.InternalServerError,
							"Could not find user data"
						)
						is UserAuthErrors.CouldNotCreateUser -> Pair(
							HttpStatusCode.InternalServerError,
							"Could not register new user"
						)
						is UserAuthErrors.UserDoesNotHaveServerAccess -> Pair(
							HttpStatusCode.Forbidden,
							"User does not have access to the Plex server"
						)
					}
					call.respond(statusCode, message)
				}.bind()
			call.sessions.set(UserSession(user.id, user.plexUsername))
			call.respondRedirect("/?login=success")
		}
	}

	get<AuthResource.Logout> {
		call.sessions.clear<UserSession>()
		call.respondRedirect("/")
	}

	authenticate("user_session") {
		post<AuthResource.Token> {
			val user = call.principal<UserSession>()
			val token = JWT.create()
				.withAudience(env.jwtAudience)
				.withIssuer(env.jwtIssuer)
				.withClaim("userId", user?.id)
				.withClaim("plexUsername", user?.plexUsername)
				.withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.sign(Algorithm.HMAC256(env.jwtTokenSecret))
			call.respond(HttpStatusCode.OK, mapOf("token" to token))
		}
	}

}