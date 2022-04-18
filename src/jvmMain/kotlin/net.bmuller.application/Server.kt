package net.bmuller.application

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmuller.application.plugins.*

fun main() {
	embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
		configureCors()
		configureContentNegotiation()
		configureLogging()
		configureCompression()
		configureRouting()
	}.start(wait = true)

}