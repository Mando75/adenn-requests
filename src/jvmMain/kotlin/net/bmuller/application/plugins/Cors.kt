package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCors() {
	install(CORS) {
		allowHost("localhost:8080")
		allowHost("bmuller.net", subDomains = listOf("plex"), schemes = listOf("https"))
		allowHeader(HttpHeaders.ContentType)
		allowHeader(HttpHeaders.Authorization)
	}
}