package net.bmuller.application.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import net.bmuller.application.config.Env
import org.koin.java.KoinJavaComponent.inject

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

fun tmdbClient(config: Env.TMDB) = object : TMDBClient {
	private val apiKeyParam = "api_key"
	private val sessionIdParam = "session_id"

	override val client = createClient {
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

class TMDBClientImpl(customConfig: TMDBClientConfig? = null) : TMDBClient, BaseHttpClient() {

	private val config: TMDBClientConfig
	private val apiKeyParam = "api_key"
	private val sessionIdParam = "session_id"

	init {
		config = customConfig ?: defaultConfig()
	}

	private fun defaultConfig(): TMDBClientConfig {
		val env: Env by inject(Env::class.java)
		val apiKey = env.tmdb.apiKey
		val host = TMDB_HOST
		val sessionToken = env.tmdb.sessionToken
		val requestToken = env.tmdb.requestToken
		return TMDBClientConfig(apiKey, host, sessionToken, requestToken)
	}

	override val client = createClient {
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

