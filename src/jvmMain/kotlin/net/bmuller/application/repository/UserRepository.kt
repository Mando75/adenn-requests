package net.bmuller.application.repository

import db.tables.UserTable
import db.tables.toAdminUser
import db.tables.toUserEntity
import entities.UserEntity
import entities.UserType
import kotlinx.coroutines.Dispatchers
import net.bmuller.application.entities.AdminUser
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


interface UserRepository {
	suspend fun createAndReturnUser(plexToken: String, newUser: UserEntity): UserEntity
	fun getAdminUser(): AdminUser
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

	override suspend fun createAndReturnUser(plexToken: String, newUser: UserEntity): UserEntity {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val userType: UserType =
				if (UserTable.selectAll().limit(1).count() <= 0) UserType.ADMIN else UserType.DEFAULT
			val id = UserTable.insertAndGetId { user ->
				user[this.plexUsername] = newUser.plexUsername
				user[this.plexId] = newUser.plexId
				user[this.plexToken] = plexToken
				user[this.email] = newUser.email
				user[this.userType] = userType
			}
			UserTable
				.slice(defaultUserSlice)
				.select { UserTable.id eq id }
				.single()
				.toUserEntity()
		}
	}

	override fun getAdminUser(): AdminUser = transaction {
		UserTable
			.select { UserTable.userType eq UserType.ADMIN }
			.single()
			.toAdminUser()
	}

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


	override suspend fun updatePlexToken(userId: Int, plexToken: String): Int {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			UserTable.update({ UserTable.id eq userId }) { row ->
				row[UserTable.plexToken] = plexToken
			}
		}
	}

}