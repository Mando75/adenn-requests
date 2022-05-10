package net.bmuller.application.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.apiV1() {
	route("/api/v1") {
		auth()
		authenticate("user_session", "bearer_token") {
			mediaItems()
			tmdb()
			users()
		}
	}
}