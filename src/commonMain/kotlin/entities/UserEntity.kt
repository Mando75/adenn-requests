package entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class UserEntity(
	val id: Int,
	val plexUsername: String,
	val plexId: Int,
	val email: String,
	val userType: UserType,
	val requestCount: Int,
	val movieQuotaLimit: Int,
	val movieQuotaDays: Int,
	val tvQuotaLimit: Int,
	val tvQuotaDays: Int,
	val createdAt: Instant,
	val modifiedAt: Instant
) {
	companion object {
		fun createNew(username: String, id: Int, userEmail: String, type: UserType = UserType.DEFAULT): UserEntity {
			return UserEntity(
				0, username, id, userEmail, type,
				0, 5, 1,
				5, 1, Clock.System.now(), Clock.System.now()
			)
		}
	}
}

enum class UserType {
	ADMIN, DEFAULT,
}