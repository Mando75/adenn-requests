package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator
import net.bmuller.application.config.Env

interface PlexClient {
	val client: HttpClient
}


fun plexClient(config: Env.Plex, engine: HttpClientEngine) = object : PlexClient {
	private val jsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
		isLenient = true
		classDiscriminator = JsonSchemaDiscriminator
	}

	override val client = createClient(engine, jsonBuilder) {
		url {
			host = config.host
			protocol = URLProtocol.HTTPS
		}
		contentType(ContentType.Application.Json)
		accept(ContentType.Application.Json)
	}
}