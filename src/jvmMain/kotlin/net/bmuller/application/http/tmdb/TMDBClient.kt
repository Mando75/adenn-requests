package net.bmuller.application.http.tmdb

import io.ktor.client.*
import io.ktor.http.*
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.http.BaseHttpClient
import org.koin.java.KoinJavaComponent.inject

const val TMDB_HOST = "api.themoviedb.org"

interface TMDBClient {
	val client: HttpClient
}

class TMDBClientImpl(customConfig: TMDBClientConfig? = null) : TMDBClient, BaseHttpClient() {

	data class TMDBClientConfig(
		val apiKey: String,
		val host: String,
		val sessionToken: String,
		val requestToken: String
	)

	private val config: TMDBClientConfig
	private val apiKeyParam = "api_key"
	private val sessionIdParam = "session_id"

	init {
		config = customConfig ?: defaultConfig()
	}

	private fun defaultConfig(): TMDBClientConfig {
		val env: EnvironmentValues by inject(EnvironmentValues::class.java)
		val apiKey = env.tmdbApiKey
		val host = TMDB_HOST
		val sessionToken = env.tmdbSessionToken
		val requestToken = env.tmdbRequestToken
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
	}

}

