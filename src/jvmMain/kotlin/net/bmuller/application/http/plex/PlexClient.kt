package net.bmuller.application.http.plex

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import net.bmuller.application.http.BaseHttpClient

interface PlexClient {
	val pinClient: HttpClient
}

const val PLEX_PIN_HOST = "plex.tv"
const val PLEX_PIN_PATH = "/api/v2"


class PlexClientImpl(customConfig: PlexClientConfig? = null) : BaseHttpClient(), PlexClient {
	data class PlexClientConfig(
		val host: String,
		val headers: Map<String, String>,
		val path: String
	)

	private val config: PlexClientConfig

	init {
		config = customConfig ?: defaultConfig()
	}

	private val plexHeaders = mapOf(
		"X-Plex-Client-Identifier" to "7909d93e-0876-42c8-99a8-ea5c1c3c3bd5",
		"X-Plex-Device" to "Web",
		"X-Plex-Platform" to "Web",
		"X-Plex-Product" to "Adenn Requests",
		"X-Plex-Version" to "0.0.1"
	)

	private fun defaultConfig() = PlexClientConfig(
		host = PLEX_PIN_HOST,
		headers = plexHeaders,
		path = PLEX_PIN_PATH
	)

	override val pinClient = createClient {
		url {
			host = config.host
			path(config.path)
			protocol = URLProtocol.HTTPS
			config.headers.forEach { (key, value) -> header(key, value) }
		}
		contentType(ContentType.Application.Json)
	}
}