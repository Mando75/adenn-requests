package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator

interface PlexClient {
	val client: HttpClient
}

private const val PLEX_PIN_HOST = "plex.tv"

data class PlexClientConfig(
	val host: String = PLEX_PIN_HOST,
)

fun plexClient(config: PlexClientConfig = PlexClientConfig()) = object : PlexClient {
	private val jsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
		isLenient = true
		classDiscriminator = JsonSchemaDiscriminator
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


class PlexClientImpl(customConfig: PlexClientConfig? = null) : BaseHttpClient(), PlexClient {


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
		classDiscriminator = JsonSchemaDiscriminator
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