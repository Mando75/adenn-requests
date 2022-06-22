package lib.apiClient

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator

val apiClient = HttpClient {
	expectSuccess = true
	install(Resources)
	install(ContentNegotiation) {
		json(Json {
			encodeDefaults = true
			ignoreUnknownKeys = true
			prettyPrint = true
			classDiscriminator = JsonSchemaDiscriminator
		})
	}
	install(DefaultRequest) {
		url {
			host = "${window.location.host}/api/v1"
		}
		contentType(ContentType.Application.Json)
	}
}