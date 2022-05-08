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
	val location: PlexCodeLocation,
	val newRegistration: Boolean?,
	val product: String,
	val trusted: Boolean
)

@kotlinx.serialization.Serializable
data class PlexCodeLocation(
	val city: String,
	val code: String,
	val coordinates: String,
	val country: String,
	@SerialName("postal_code") val postalCode: String,
	val subdivisions: String,
	@SerialName("time_zone") val timeZone: String
)

data class PlexClientHeaders(
	val clientId: String = "7909d93e-0876-42c8-99a8-ea5c1c3c3bd5",
	val product: String = "Adenn Requests",
	val device: String = "Web",
	val version: String = "0.0.1",
	val platform: String = "Web"
)

@kotlinx.serialization.Serializable
data class PlexAccountResponse(
	val user: PlexUser
)

@kotlinx.serialization.Serializable
data class PlexUser(
	val id: Int,
	val uuid: String,
	val email: String,
	@SerialName("joined_at")
	val joinedAt: String,
	val username: String,
	val title: String,
	val thumb: String,
	val hasPassword: String,
	val authToken: String,
	val subscription: PlexUserSubscription,
	val roles: PlexUserRoles,
	val entitlements: List<String>
)

@kotlinx.serialization.Serializable
data class PlexUserRoles(
	val roles: List<String>
)

@kotlinx.serialization.Serializable
data class PlexUserSubscription(
	val active: Boolean,
	val status: String,
	val plan: String,
	val features: List<String>
)

@kotlinx.serialization.Serializable
data class PlexFriendsResponse(
	@SerialName("MediaContainer")
	val mediaContainer: PlexMediaContainer
)

@kotlinx.serialization.Serializable
data class PlexMediaContainer(
	@SerialName("User")
	val user: List<PlexMediaContainerUserWrapper>
)

@kotlinx.serialization.Serializable
data class PlexMediaContainerUserWrapper(
	@SerialName("$")
	val userWrapper: PlexMediaContainerUser,
	@SerialName("Server")
	val server: ServerResponseWrapper?
)

@kotlinx.serialization.Serializable
data class PlexMediaContainerUser(
	val id: String,
	val title: String,
	val username: String,
	val email: String,
	val thumb: String
)

@kotlinx.serialization.Serializable
data class ServerResponseWrapper(
	@SerialName("$")
	val serverWrapper: ServerResponse
)

@kotlinx.serialization.Serializable
data class ServerResponse(
	val id: String,
	val serverId: String,
	val machineIdentifier: String,
	val name: String,
	val lastSeenAt: String,
	val numLibraries: String,
	val owned: String
)