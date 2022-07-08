package net.bmuller.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.bmuller.application.config.Env
import net.bmuller.application.lib.awaitShutdown
import net.bmuller.application.plugins.*

fun main(): Unit = runBlocking(Dispatchers.Default) {
	val env = Env()
	embeddedServer(
		Netty,
		port = env.http.port,
		host = env.http.host,
		watchPaths = listOf("classes")
	) { mainModule() }.awaitShutdown()
}

fun Application.mainModule() {
	configureDI()
	configureDatabase()
	configureAuthentication()
	configureCors()
	configureContentNegotiation()
	configureLogging()
	configureCompression()
	configureResources()
	configureRouting()
}