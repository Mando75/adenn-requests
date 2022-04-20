package net.bmuller.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmuller.application.db.postgres.util.createDb
import net.bmuller.application.plugins.*

fun main() {
	embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
	createDb()
	configureCors()
	configureContentNegotiation()
	configureLogging()
	configureCompression()
	configureResources()
	configureRouting()
}