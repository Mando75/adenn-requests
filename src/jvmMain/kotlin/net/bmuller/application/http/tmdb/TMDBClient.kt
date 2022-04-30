package net.bmuller.application.http.tmdb

import io.ktor.client.*
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.http.BaseHttpClient
import org.koin.java.KoinJavaComponent.inject

const val TMDB_BASE_URL = "https://api.themoviedb.org/3"

interface TMDBClient {
	val client: HttpClient
}

class TMDBClientImpl(customConfig: TMDBClientConfig? = null) : TMDBClient, BaseHttpClient() {

	data class TMDBClientConfig(
		val apiKey: String,
		val baseUrl: String,
		val sessionToken: String,
		val requestToken: String
	);

	private val config: TMDBClientConfig
	private val apiKeyParam = "api_key"
	private val sessionIdParam = "session_id"

	init {
		config = customConfig ?: defaultConfig()
	}

	private fun defaultConfig(): TMDBClientConfig {
		val env: EnvironmentValues by inject(EnvironmentValues::class.java)
		val apiKey = env.tmdbApiKey
		val baseUrl = TMDB_BASE_URL
		val sessionToken = env.tmdbSessionToken
		val requestToken = env.tmdbRequestToken
		return TMDBClientConfig(apiKey, baseUrl, sessionToken, requestToken)
	}

	override val client = createResourceClient {
		url {
			host = config.baseUrl
			parameters.append(apiKeyParam, config.apiKey)
			parameters.append(sessionIdParam, config.sessionToken)
		}
	}

}

