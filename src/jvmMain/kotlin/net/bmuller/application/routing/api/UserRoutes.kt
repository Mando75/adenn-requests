package net.bmuller.application.routing.api

import arrow.core.continuations.either
import http.UserResource
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import net.bmuller.application.lib.respond
import net.bmuller.application.plugins.parseUserAuth
import net.bmuller.application.service.IUserService

fun Route.users(userService: IUserService) {

	get<UserResource.Me> {
		either {
			val session = call.parseUserAuth().bind()
			userService.me(session.id).bind()
		}.respond()
	}
}