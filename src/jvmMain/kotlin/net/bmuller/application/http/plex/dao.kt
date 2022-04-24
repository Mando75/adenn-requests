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