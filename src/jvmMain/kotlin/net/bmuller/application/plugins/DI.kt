package net.bmuller.application.plugins

import io.ktor.server.application.*
import net.bmuller.application.di.databaseModule
import net.bmuller.application.di.envModule
import net.bmuller.application.di.httpModule
import net.bmuller.application.di.repoModule

fun Application.configureDI() {
	install(Koin) {
		modules = arrayListOf(envModule, databaseModule, httpModule, repoModule)
	}
}