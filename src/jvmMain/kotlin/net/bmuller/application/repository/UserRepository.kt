package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.rightIfNotNull
import db.tables.UserTable
import db.tables.toAdminUser
import db.tables.toUserEntity
import entities.UserEntity
import entities.UserType
import kotlinx.coroutines.Dispatchers
import net.bmuller.application.entities.AdminUser
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.EntityNotFound
import net.bmuller.application.lib.Unknown
import net.bmuller.application.lib.catchUnknown
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface UserRepository {
	suspend fun createAndReturnUser(
		plexUsername: String,
		plexId: Int,
		plexToken: String,
		email: String
	): Either<Unknown, UserEntity>

	suspend fun getAdminUser(): Either<DomainError, AdminUser>
	suspend fun getUserById(userId: Int): Either<DomainError, UserEntity>
	suspend fun getUserByPlexId(plexUserId: Int): Either<DomainError, UserEntity>

	suspend fun getUserPlexToken(userId: Int): Either<DomainError, String?>

	suspend fun getUserAuthVersion(userId: Int): Either<DomainError, Int?>
	suspend fun updatePlexToken(userId: Int, plexToken: String): Either<DomainError, Int>

}

fun userRepository(exposed: Database) = object : UserRepository {
	private val defaultUserSlice = listOf(
		UserTable.id,
		UserTable.plexUsername,
		UserTable.plexId,
		UserTable.email,
		UserTable.userType,
		UserTable.requestCount,
		UserTable.movieQuotaLimit,
		UserTable.movieQuotaDays,
		UserTable.tvQuotaDays,
		UserTable.tvQuotaLimit,
		UserTable.createdAt,
		UserTable.modifiedAt,
		UserTable.authVersion
	)

	override suspend fun createAndReturnUser(
		plexUsername: String,
		plexId: Int,
		plexToken: String,
		email: String
	): Either<Unknown, UserEntity> =
		Either.catchUnknown {
			newSuspendedTransaction(Dispatchers.IO, exposed) {
				val userType: UserType =
					if (UserTable.selectAll().limit(1).count() <= 0) UserType.ADMIN else UserType.DEFAULT
				val id = UserTable.insertAndGetId { user ->
					user[this.plexUsername] = plexUsername
					user[this.plexId] = plexId
					user[this.plexToken] = plexToken
					user[this.email] = email
					user[this.userType] = userType
				}
				UserTable
					.slice(defaultUserSlice)
					.select { UserTable.id eq id }
					.single()
					.toUserEntity()
			}
		}

	override suspend fun getAdminUser(): Either<DomainError, AdminUser> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			UserTable
				.select { UserTable.userType eq UserType.ADMIN }
				.limit(1)
				.single()
				.toAdminUser()
		}
	}

	override suspend fun getUserById(userId: Int): Either<DomainError, UserEntity> = Either.catchUnknown {
		val user = newSuspendedTransaction(Dispatchers.IO, exposed) {
			UserTable.slice(defaultUserSlice).select { UserTable.id eq userId }.singleOrNull()
		}
		return user?.toUserEntity().rightIfNotNull { EntityNotFound(userId.toString()) }
	}

	override suspend fun getUserByPlexId(plexUserId: Int): Either<DomainError, UserEntity> = Either.catchUnknown {
		val user = newSuspendedTransaction(Dispatchers.IO, exposed) {
			UserTable.slice(defaultUserSlice).select { UserTable.plexId eq plexUserId }.singleOrNull()
		}
		return user?.toUserEntity().rightIfNotNull { EntityNotFound(plexUserId.toString()) }
	}

	override suspend fun getUserPlexToken(userId: Int): Either<DomainError, String?> = Either.catchUnknown {
		val result = newSuspendedTransaction(Dispatchers.IO, exposed) {
			UserTable.slice(UserTable.plexToken).select { UserTable.id eq userId }.singleOrNull()
		}
		result?.get(UserTable.plexToken)
	}

	override suspend fun getUserAuthVersion(userId: Int): Either<DomainError, Int?> = Either.catchUnknown {
		val result = newSuspendedTransaction(Dispatchers.IO, exposed) {
			UserTable.slice(UserTable.authVersion).select { UserTable.id eq userId }.singleOrNull()
		}
		result?.get(UserTable.authVersion)
	}


	override suspend fun updatePlexToken(userId: Int, plexToken: String): Either<DomainError, Int> =
		Either.catchUnknown {
			newSuspendedTransaction(Dispatchers.IO, exposed) {
				UserTable.update({ UserTable.id eq userId }) { row ->
					row[UserTable.plexToken] = plexToken
				}
			}
		}
}