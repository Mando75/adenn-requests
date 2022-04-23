package net.bmuller.application.http.tmdb

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import io.ktor.client.request.*
import io.ktor.client.statement.*
import net.bmuller.application.config.MissingEnvException
import net.bmuller.application.http.BaseHttpClient

data class TMDBClientConfig(
	val apiKey: String,
	val sessionToken: String,
	val requestToken: String
)

sealed class TMDBClientError {
	data class MissingConfig(val configKey: String) : TMDBClientError()
	object Unknown : TMDBClientError()
}


class TMDBClient : BaseHttpClient() {

	val API_KEY = "api_key"
	val SESSION_ID = "session_id"

	fun tmdbConfig(): Either<MissingEnvException, TMDBClientConfig> = either.eager {
		val apiKey = configProvider.getValue("MOVIE_DB_API_KEY").bind()
		val sessionToken = configProvider.getValue("MOVIE_DB_SESSION_TOKEN").bind()
		val requestToken = configProvider.getValue("MOVIE_DB_REQUEST_TOKEN").bind()

		return@eager TMDBClientConfig(apiKey, sessionToken, requestToken)
	}

	suspend inline fun <reified T> exec(builder: HttpRequestBuilder): Either<TMDBClientError, Pair<T, HttpResponse>> =
		tmdbConfig().mapLeft { envException ->
			return when (envException) {
				is MissingEnvException.NullKey -> TMDBClientError.MissingConfig(envException.missingKey).left()
			}
		}.flatMap { config ->
			builder.parameter(API_KEY, config.apiKey)
			builder.parameter(SESSION_ID, config.sessionToken)
			makeRequest<T>(builder)
		}.mapLeft { TMDBClientError.Unknown }.map { it }
}