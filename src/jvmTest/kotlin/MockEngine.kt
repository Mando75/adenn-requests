import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmuller.application.entities.PlexCodeResponse

val mockEngine = MockEngine { request ->
	val (code, content) = when (request.url.host) {
		"plex.tv" -> plexRouteHandler(request.url.encodedPath)
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
		Json.encodeToString(
			PlexCodeResponse(
				authToken = "test-plex-auth-token",
				clientIdentifier = "test-id",
				code = "test-code",
				createdAt = Clock.System.now().toString(),
				expiresIn = 10000,
				id = 1,
				location = PlexCodeResponse.Location(
					city = "New York",
					code = "ZABD",
					coordinates = "latlong",
					country = "United States",
					postalCode = "12345",
					subdivisions = "",
					timeZone = "EST"
				),
				newRegistration = false,
				product = "PlexTV",
				trusted = true,
				expiresAt = ""
			)
		)
	)
	else -> Pair(HttpStatusCode(418, "I'm a teapot"), """{"err": "invalid path"}""")
}