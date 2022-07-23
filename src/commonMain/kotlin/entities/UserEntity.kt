package entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class UserEntity(
	val id: Int,
	val plexUsername: String,
	val plexId: Int,
	val profilePicUrl: String? = null,
	val email: String,
	val userType: UserType = UserType.DEFAULT,
	val requestCount: Int = 0,
	val movieQuotaLimit: Int = 5,
	val movieQuotaDays: Int = 1,
	val tvQuotaLimit: Int = 5,
	val tvQuotaDays: Int = 1,
	val createdAt: Instant = Clock.System.now(),
	val modifiedAt: Instant = Clock.System.now(),
	val authVersion: Int = 0
)

enum class UserType {
	ADMIN, DEFAULT,
}