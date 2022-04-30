package net.bmuller.application.plugins

import io.ktor.server.application.*
import net.bmuller.application.di.*

fun Application.configureDI() {
	install(Koin) {
		modules = arrayListOf(envModule, databaseModule, httpModule, repoModule, serviceModule)
	}
}