package net.bmuller.application.routing.v1

import arrow.core.continuations.either
import http.UserResource
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import net.bmuller.application.lib.error.DomainError
import net.bmuller.application.lib.respond
import net.bmuller.application.plugins.parseUserAuth
import net.bmuller.application.service.IUserService

fun Route.users(userService: IUserService) {

	get<UserResource.Me> {
		either<DomainError, Unit> {
			val session = call.parseUserAuth().bind()
			userService.me(session.id)
		}.respond()
	}
}