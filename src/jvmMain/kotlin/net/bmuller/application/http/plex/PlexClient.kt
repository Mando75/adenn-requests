package net.bmuller.application.http.plex

import arrow.core.Either
import io.ktor.client.request.*
import io.ktor.client.statement.*
import net.bmuller.application.http.BaseHttpClient

data class PlexClientError(val message: String?)

class PlexClient : BaseHttpClient() {
	val headers = mapOf(
		"X-Plex-Client-Identifier" to "7909d93e-0876-42c8-99a8-ea5c1c3c3bd5",
		"X-Plex-Device" to "Web",
		"X-Plex-Platform" to "Web",
		"X-Plex-Product" to "Adenn Requests",
		"X-Plex-Version" to "0.0.1"
	)

	suspend inline fun <reified T> exec(builder: HttpRequestBuilder): Either<PlexClientError, Pair<T, HttpResponse>> {
		headers.map { (key, value) -> builder.header(key, value) }
		return makeRequest<T>(builder)
			.mapLeft { error -> PlexClientError(error.message) }
	}
}