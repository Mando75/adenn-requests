package net.bmuller.application.repository

import db.tables.UserTable
import db.tables.toUserEntity
import entities.UserEntity
import entities.UserType
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

data class NewUser(
	val plexUsername: String,
	val plexId: Int,
	val plexToken: String,
	val email: String,
)

interface UserRepository {
	suspend fun createAndReturnUser(newUser: NewUser): UserEntity
	suspend fun getUserById(userId: Int): UserEntity?
	suspend fun getUserByPlexId(plexUserId: Int): UserEntity?

	suspend fun getUserPlexToken(userId: Int): String?
	suspend fun updatePlexToken(userId: Int, plexToken: String): Int

}

class UserRepositoryImpl : BaseRepository(), UserRepository {

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
		UserTable.modifiedAt
	)

	override suspend fun getUserById(userId: Int): UserEntity? {
		val user = newSuspendedTransaction(Dispatchers.IO, db) {
			UserTable.slice(defaultUserSlice).select { UserTable.id eq userId }.singleOrNull()
		}
		return user?.toUserEntity()
	}

	override suspend fun getUserByPlexId(plexUserId: Int): UserEntity? {
		val user = newSuspendedTransaction(Dispatchers.IO, db) {
			UserTable.slice(defaultUserSlice).select { UserTable.plexId eq plexUserId }.singleOrNull()
		}
		return user?.toUserEntity()
	}

	override suspend fun getUserPlexToken(userId: Int): String? {
		val result = newSuspendedTransaction(Dispatchers.IO, db) {
			UserTable.slice(UserTable.plexToken).select { UserTable.id eq userId }.singleOrNull()
		}
		return result?.get(UserTable.plexToken)
	}

	override suspend fun createAndReturnUser(newUser: NewUser): UserEntity {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val id = UserTable.insertAndGetId { user ->
				user[plexUsername] = newUser.plexUsername
				user[plexId] = newUser.plexId
				user[plexToken] = newUser.plexToken
				user[email] = newUser.email
				user[userType] = UserType.DEFAULT
			}
			UserTable
				.slice(defaultUserSlice)
				.select { UserTable.id eq id }
				.single()
				.toUserEntity()
		}
	}

	override suspend fun updatePlexToken(userId: Int, plexToken: String): Int {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			UserTable.update({ UserTable.id eq userId }) { row ->
				row[UserTable.plexToken] = plexToken
			}
		}
	}

}