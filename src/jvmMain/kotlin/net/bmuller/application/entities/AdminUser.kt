package net.bmuller.application.entities

import entities.UserType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class AdminUser(
	val id: Int,
	val plexUsername: String,
	val plexId: Int,
	val plexToken: String,
	val email: String,
	val userType: UserType = UserType.DEFAULT,
	val requestCount: Int = 0,
	val movieQuotaLimit: Int = 5,
	val movieQuotaDays: Int = 1,
	val tvQuotaLimit: Int = 5,
	val tvQuotaDays: Int = 1,
	val createdAt: Instant = Clock.System.now(),
	val modifiedAt: Instant = Clock.System.now()
)
