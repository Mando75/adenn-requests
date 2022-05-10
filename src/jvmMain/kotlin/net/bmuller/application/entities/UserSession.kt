package net.bmuller.application.entities

import io.ktor.server.auth.*

data class UserSession(val id: Int, val plexUsername: String, val version: Int) : Principal
