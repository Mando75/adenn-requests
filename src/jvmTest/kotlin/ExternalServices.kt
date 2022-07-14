import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import net.bmuller.application.config.Env
import net.bmuller.application.entities.PlexAccountResponse
import net.bmuller.application.entities.PlexCodeResponse
import net.bmuller.application.entities.PlexFriendsResponse
import net.bmuller.application.entities.PlexUser
import net.bmuller.application.repository.AuthPinResources
import net.bmuller.application.repository.PlexTVResources

fun Route.plexExternalServices(env: Env.Plex) {
	post<AuthPinResources.V2.Pins> {
		call.respond(
			HttpStatusCode.OK,
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
	}

	get<AuthPinResources.V2.Pins.ID> { _ ->
		call.respond(
			HttpStatusCode.OK,
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
	}

	get<PlexTVResources.Users.Account> { _ ->
		call.respond(
			HttpStatusCode.OK,
			PlexAccountResponse(
				user = PlexUser(
					username = "testuser",
					id = 1,
					authToken = "plexauthtoken",
					email = "test@bmuller.net",
					uuid = "random-guid",
					joinedAt = "",
					title = "",
					hasPassword = "",
					subscription = PlexUser.Subscription(
						active = true,
						status = "active",
						plan = "plexpass",
						features = emptyList()
					),
					roles = PlexUser.Roles(roles = emptyList()),
					entitlements = emptyList(),
					thumb = ""
				)
			)
		)
	}

	get<PlexTVResources.PMS.Friends.All> { _ ->
		call.respond(
			HttpStatusCode.OK,
			PlexFriendsResponse(
				friendlyName = "testplexserver",
				identifier = env.machineId,
				machineIdentifier = env.machineId,
				totalSize = 1,
				size = 1,
				users = listOf(
					PlexFriendsResponse.User(
						id = 1,
						title = "Test User",
						username = "testuser",
						email = "test@bmuller.net",
						recommendationsPlaylistId = null,
						thumb = "",
						server = PlexFriendsResponse.Server(
							id = 2,
							serverId = 3,
							machineIdentifier = env.machineId,
							name = "Test Server",
							lastSeenAt = "",
							numLibraries = 1,
							owned = 1
						)
					)
				)
			)
		)
	}
}