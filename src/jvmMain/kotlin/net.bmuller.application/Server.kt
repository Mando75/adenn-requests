package net.bmuller.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import net.bmuller.application.plugins.configureContentNegotiation
import net.bmuller.application.plugins.configureCors
import net.bmuller.application.plugins.configureRouting

fun main() {
	embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
		configureCors()
		configureContentNegotiation()
		configureRouting()
		install(CallLogging) {
			filter { call -> call.request.path().startsWith("/api/v1") }
		}
	}.start(wait = true)

}