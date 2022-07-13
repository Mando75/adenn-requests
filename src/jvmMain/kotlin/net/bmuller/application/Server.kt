package net.bmuller.application

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.bmuller.application.config.Env
import net.bmuller.application.config.RunEnv
import net.bmuller.application.di.Dependencies
import net.bmuller.application.di.dependencies
import net.bmuller.application.lib.awaitShutdown
import net.bmuller.application.plugins.*

fun main(): Unit = runBlocking(Dispatchers.Default) {
	val env = Env()
	val watchPaths = if (env.runEnv == RunEnv.DEVELOPMENT) listOf("classes") else emptyList()
	dependencies(env).use { module ->
		embeddedServer(
			Netty,
			port = env.http.port,
			host = env.http.host,
			watchPaths = watchPaths
		) { mainModule(module) }.awaitShutdown()
	}
}

fun Application.mainModule(dependencies: Dependencies) {
	configureAuthentication(dependencies.env, dependencies.userAuthService)
	configureCors()
	configureContentNegotiation()
	configureLogging()
	configureCompression()
	configureResources()
	configureRouting(dependencies)
}