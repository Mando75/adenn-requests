package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

abstract class BaseHttpClient {
	private val defaultJsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
	}

	fun createClient(
		jsonBuilder: Json = defaultJsonBuilder,
		defaultRequestBlock: DefaultRequest.DefaultRequestBuilder.() -> Unit
	) = HttpClient(CIO) {
		install(Resources)
		install(ContentNegotiation) {
			json(jsonBuilder)
		}
		install(DefaultRequest) {
			defaultRequestBlock()
		}
	}
}