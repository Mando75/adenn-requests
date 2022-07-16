package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.http.*
import net.bmuller.application.config.Env

const val TMDB_HOST = "api.themoviedb.org"

interface TMDBClient {
	val client: HttpClient
}

data class TMDBClientConfig(
	val apiKey: String,
	val host: String,
	val sessionToken: String,
	val requestToken: String
)

fun tmdbClient(config: Env.TMDB, engine: HttpClientEngine) = object : TMDBClient {
	private val apiKeyParam = "api_key"
	private val sessionIdParam = "session_id"

	override val client = createClient(engine) {
		url {
			host = config.host
			protocol = URLProtocol.HTTPS
			parameters.append(apiKeyParam, config.apiKey)
			parameters.append(sessionIdParam, config.sessionToken)
		}
		contentType(ContentType.Application.Json)
		accept(ContentType.Application.Json)
	}
}