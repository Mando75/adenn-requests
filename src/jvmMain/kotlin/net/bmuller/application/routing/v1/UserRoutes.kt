package net.bmuller.application.routing.v1

import http.UserResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.plugins.parseUserAuth
import net.bmuller.application.service.IUserService

fun Route.users(userService: IUserService) {

	get<UserResource.Me> {
		val session = call.parseUserAuth()
		userService.me(session?.id).mapLeft { error ->
			call.application.environment.log.error(error.message)
			call.respond(HttpStatusCode.InternalServerError)
		}.map { userEntity ->
			userEntity?.let { call.respond(HttpStatusCode.OK, it) } ?: call.respond(HttpStatusCode.Forbidden)
		}
	}
}