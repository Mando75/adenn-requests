package net.bmuller.application.entities

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PlexCodeResponse(
	val authToken: String?,
	val clientIdentifier: String,
	val code: String,
	val createdAt: String,
	val expiresAt: String,
	val expiresIn: Long,
	val id: Long,
	val location: Location,
	val newRegistration: Boolean?,
	val product: String,
	val trusted: Boolean
) {
	@kotlinx.serialization.Serializable
	data class Location(
		val city: String,
		val code: String,
		val coordinates: String,
		val country: String,
		@SerialName("postal_code") val postalCode: String,
		val subdivisions: String,
		@SerialName("time_zone") val timeZone: String
	)
}


data class PlexClientHeaders(
	val clientId: String,
	val product: String,
	val device: String,
	val version: String,
	val platform: String
)

@kotlinx.serialization.Serializable
data class PlexAccountResponse(
	val user: PlexUser
)

@kotlinx.serialization.Serializable
data class PlexUser(
	val username: String,
	val id: Int,
	val authToken: String,
	val email: String,
	val uuid: String,
	@SerialName("joined_at")
	val joinedAt: String,
	val title: String,
	val thumb: String,
	val hasPassword: String,
	val subscription: Subscription,
	val roles: Roles,
	val entitlements: List<String>
) {

	@kotlinx.serialization.Serializable
	data class Roles(
		val roles: List<String>
	)

	@kotlinx.serialization.Serializable
	data class Subscription(
		val active: Boolean,
		val status: String,
		val plan: String?,
		val features: List<String>
	)
}


@kotlinx.serialization.Serializable
@SerialName("MediaContainer")
data class PlexFriendsResponse(
	val friendlyName: String,
	val identifier: String,
	val machineIdentifier: String,
	val totalSize: Int,
	val size: Int,
	val users: List<User>
) {
	@kotlinx.serialization.Serializable
	data class User(
		val id: Int,
		val title: String,
		val username: String,
		val email: String,
		val recommendationsPlaylistId: String?,
		val thumb: String,
		val server: Server
	)

	@kotlinx.serialization.Serializable
	data class Server(
		val id: Int,
		val serverId: Int,
		val machineIdentifier: String,
		val name: String,
		val lastSeenAt: String,
		val numLibraries: Int,
		val owned: Int
	)
}


