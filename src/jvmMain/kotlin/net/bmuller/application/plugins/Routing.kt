package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class TestData(val id: Int, val name: String)

fun Application.configureRouting() {
	routing {
		get("/api/v1") {
			call.respond(HttpStatusCode.OK, TestData(1, "Bryan"))
		}

		static("/static") {
			resources()
		}
	}
}