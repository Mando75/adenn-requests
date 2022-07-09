package net.bmuller.application.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import net.bmuller.application.di.Dependencies

fun Routing.apiV1(dependencies: Dependencies) {
	route("/api/v1") {
		auth(dependencies.env, dependencies.plexOAuthService, dependencies.userAuthService)
		authenticate("user_session", "bearer_token") {
			users()
			search()
			requests()
		}
	}
}