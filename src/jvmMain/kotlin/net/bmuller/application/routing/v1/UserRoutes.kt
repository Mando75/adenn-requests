package net.bmuller.application.routing.v1

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import net.bmuller.application.entities.UserSession
import net.bmuller.application.plugins.inject
import net.bmuller.application.service.UserService

@kotlinx.serialization.Serializable
@Resource("/users")
class UserResource {

	@kotlinx.serialization.Serializable
	@Resource("me")
	class Me(@Suppress("unused") val parent: UserResource = UserResource())
}

fun Route.users() {
	val userService: UserService by inject()

	get<UserResource.Me> {
		val session = call.principal<UserSession>()
		userService.me(session?.id).mapLeft { error ->
			call.application.environment.log.error(error.message)
			call.respond(HttpStatusCode.InternalServerError)
		}.map { userEntity ->
			call.application.environment.log.info("User not found, logging out session")
			call.sessions.clear<UserSession>()
			userEntity?.let { call.respond(HttpStatusCode.OK, it) } ?: call.respond(HttpStatusCode.Forbidden)
		}

	}
}