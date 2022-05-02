package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

interface PlexClient {
	val client: HttpClient
}

const val PLEX_PIN_HOST = "plex.tv"


class PlexClientImpl(customConfig: PlexClientConfig? = null) : BaseHttpClient(), PlexClient {
	data class PlexClientConfig(
		val host: String,
	)


	private val config: PlexClientConfig

	init {
		config = customConfig ?: defaultConfig()
	}

	private fun defaultConfig() = PlexClientConfig(
		host = PLEX_PIN_HOST,
	)

	private val jsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
		isLenient = true
	}

	override val client = createClient(jsonBuilder) {
		url {
			host = config.host
			protocol = URLProtocol.HTTPS
		}
		contentType(ContentType.Application.Json)
		accept(ContentType.Application.Json)
	}
}