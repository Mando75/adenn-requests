package entities

import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class UserEntity(
	val id: Int,
	val plexUsername: String,
	val email: String,
	val userType: UserType,
	val requestCount: Int,
	val movieQuotaLimit: Int,
	val movieQuotaDays: Int,
	val tvQuotaLimit: Int,
	val tvQuotaDays: Int,
	val createdAt: Instant,
	val modifiedAt: Instant
)

enum class UserType {
	ADMIN,
	DEFAULT,
}