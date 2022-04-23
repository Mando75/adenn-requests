package net.bmuller.application.entities

data class User(
	val id: Int,
	val externalId: String,
	val name: String,
	val email: String
)
