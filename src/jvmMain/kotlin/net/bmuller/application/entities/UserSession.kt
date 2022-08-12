package net.bmuller.application.entities

import entities.UserType
import io.ktor.server.auth.*

data class UserSession(val id: Int, val plexUsername: String, val version: Int, val role: UserType = UserType.DEFAULT) :
	Principal
