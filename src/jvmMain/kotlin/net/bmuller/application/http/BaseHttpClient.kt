package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator


private val defaultJsonBuilder = Json {
	encodeDefaults = true
	ignoreUnknownKeys = true
	prettyPrint = true
	classDiscriminator = JsonSchemaDiscriminator
}

fun createClient(
	engine: HttpClientEngine,
	jsonBuilder: Json = defaultJsonBuilder,
	defaultRequestBlock: DefaultRequest.DefaultRequestBuilder.() -> Unit
) = HttpClient(engine) {
	expectSuccess = true
	install(Resources)
	install(ContentNegotiation) {
		json(jsonBuilder)
		xml()
	}
	install(DefaultRequest) {
		defaultRequestBlock()
	}
}