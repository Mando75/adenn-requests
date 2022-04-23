package net.bmuller.application.plugins

import io.ktor.server.application.*
import net.bmuller.application.di.configModule
import net.bmuller.application.di.databaseModule
import net.bmuller.application.di.repoModule
import net.bmuller.application.di.tmdbModule

fun Application.configureDI() {
	install(Koin) {
		modules = arrayListOf(configModule, databaseModule, tmdbModule, repoModule)
	}
}