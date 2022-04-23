package net.bmuller.application.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

fun Application.configureDatabase() {
	val db: Database by inject()
	log.info("Connected to database ${db.name}")
}