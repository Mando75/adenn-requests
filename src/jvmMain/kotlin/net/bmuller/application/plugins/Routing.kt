package net.bmuller.application.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import net.bmuller.application.di.Dependencies
import net.bmuller.application.routing.api.api


fun Application.configureRouting(dependencies: Dependencies) {

	routing {
		singlePageApplication {
			useResources = true
			defaultPage = "index.html"
		}

		// Configure api routes
		api(dependencies)

		static("/") {
			resources("")
		}
	}
}