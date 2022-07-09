package net.bmuller.application.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import net.bmuller.application.di.Dependencies

fun Routing.apiV1(module: Dependencies) {
	route("/api/v1") {
		auth(module.plexOAuthService, module.userAuthService)
		authenticate("user_session", "bearer_token") {
			users(module.userService)
			search(module.searchService)
			requests(module.requestService)
		}
	}
}