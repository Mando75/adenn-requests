package support

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.serialization.json.Json

val apiClient = HttpClient {
	expectSuccess = true
	install(Resources)
	install(ContentNegotiation) {
		json(Json {
			encodeDefaults = true
			ignoreUnknownKeys = true
			prettyPrint = true
		})
	}
	install(DefaultRequest) {
		url {
			host = "${window.location.host}/api/v1"
		}
	}
}