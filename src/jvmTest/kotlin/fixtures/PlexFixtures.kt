package fixtures

import kotlinx.datetime.Clock
import net.bmuller.application.entities.PlexAccountResponse
import net.bmuller.application.entities.PlexCodeResponse
import net.bmuller.application.entities.PlexFriendsResponse
import net.bmuller.application.entities.PlexUser

val plexCodeResponse = PlexCodeResponse(
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

val plexAccountResponse = PlexAccountResponse(
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
			features = listOf("test-feature")
		),
		roles = PlexUser.Roles(roles = listOf("test-user")),
		entitlements = listOf("test-entitlement"),
		thumb = ""
	)
)


val plexFriendsResponse =
	PlexFriendsResponse(
		friendlyName = "testplexserver",
		identifier = "my-plex-machine-id",
		machineIdentifier = "my-plex-machine-id",
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
					machineIdentifier = "my-plex-machine-id",
					name = "Test Server",
					lastSeenAt = "",
					numLibraries = 1,
					owned = 1
				)
			)
		)
	)
