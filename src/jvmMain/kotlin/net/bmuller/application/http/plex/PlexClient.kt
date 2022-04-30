package net.bmuller.application.http.plex

import io.ktor.client.*
import io.ktor.http.*
import net.bmuller.application.http.BaseHttpClient

interface PlexClient {
	val pinClient: HttpClient
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

	override val pinClient = createClient {
		url {
			host = config.host
			protocol = URLProtocol.HTTPS
		}
		contentType(ContentType.Application.Json)
	}
}