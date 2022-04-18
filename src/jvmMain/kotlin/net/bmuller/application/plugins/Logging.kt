package net.bmuller.application.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*

fun Application.configureLogging() {
	install(CallLogging) {
		filter { call -> call.request.path().startsWith("/api") }
	}
}