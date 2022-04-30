package net.bmuller.application.http.plex

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