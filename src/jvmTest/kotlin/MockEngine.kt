import fixtures.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmuller.application.lib.GenericErrorModel

val mockEngine = MockEngine { request ->
	val (code, content) = when (request.url.host) {
		"plex.tv" -> plexRouteHandler(request.url.encodedPath)
		"api.themoviedb.org" -> tmdbRouteHandler(request.url.encodedPath)
		else -> Pair(HttpStatusCode(418, "I'm a teapot"), """{"err": "Test Response"}""")
	}

	respond(
		content = ByteReadChannel(content),
		status = code,
		headers = headersOf(HttpHeaders.ContentType, "application/json")
	)
}

private fun plexRouteHandler(path: String): Pair<HttpStatusCode, String> = when (path) {
	"/api/v2/pins" -> Pair(
		HttpStatusCode.Created,
		Json.encodeToString(plexCodeResponse)
	)
	"/api/v2/pins/1" -> Pair(
		HttpStatusCode.OK, Json.encodeToString(plexCodeResponse)
	)
	"/users/account.json" -> Pair(
		HttpStatusCode.OK, Json.encodeToString(plexAccountResponse)
	)
	"/pms/friends/all" -> Pair(
		HttpStatusCode.OK, Json.encodeToString(plexFriendsResponse)
	)
	else -> Pair(
		HttpStatusCode(418, "Missing Plex Route Mock"),
		Json.encodeToString(GenericErrorModel("Missing Plex Path", path))
	)
}

private fun tmdbRouteHandler(path: String): Pair<HttpStatusCode, String> = when (path) {
	"/3/search/multi" -> Pair(HttpStatusCode.OK, searchMultiResponse)
	"/3/search/movie" -> Pair(HttpStatusCode.OK, searchMovieResult)
	"/3/search/tv" -> Pair(HttpStatusCode.OK, searchTVResult)
	else -> Pair(
		HttpStatusCode(418, "Missing TMDB Route Mock"),
		Json.encodeToString(GenericErrorModel("Missing TMDB Route mock", path))
	)
}
