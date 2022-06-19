package net.bmuller.application.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator

fun Application.configureContentNegotiation() {
	install(ContentNegotiation) {
		json(Json {
			prettyPrint = true
			ignoreUnknownKeys = true
			classDiscriminator = JsonSchemaDiscriminator
		})
	}
}