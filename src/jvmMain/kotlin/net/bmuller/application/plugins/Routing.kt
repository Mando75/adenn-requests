package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.routing.v1.apiV1
import org.jetbrains.exposed.sql.Database


fun Application.configureRouting() {
	val db: Database by inject()

	routing {
		// Configure api routes
		apiV1()

		get("/health") {
			if (db.url.isNotEmpty()) {
				return@get call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
			}
			return@get call.respond(HttpStatusCode.InternalServerError)
		}

		singlePageApplication {
			useResources = true
			defaultPage = "index.html"
		}

		static("/") {
			resources("")
		}
	}
}