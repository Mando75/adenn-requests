package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import db.tables.UserTable
import db.tables.toUserEntity
import entities.UserEntity
import entities.UserType
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

data class NewUser(
	val plexUsername: String,
	val plexId: Int,
	val plexToken: String,
	val email: String,
)

sealed class UserRepositoryErrors {
	object RecordNotFound : UserRepositoryErrors()
	data class Unknown(val message: String?) : UserRepositoryErrors()
}

interface UserRepository {
	suspend fun getUserById(userId: Int): Either<UserRepositoryErrors, UserEntity>
	suspend fun createUser(newUser: NewUser): Either<UserRepositoryErrors.Unknown, EntityID<Int>>
}

class UserRepositoryImpl : BaseRepository(), UserRepository {
	override suspend fun getUserById(userId: Int): Either<UserRepositoryErrors, UserEntity> {
		return try {
			val user = newSuspendedTransaction(Dispatchers.IO, db) {
				UserTable.select { UserTable.id eq userId }.singleOrNull()
			}

			user?.toUserEntity()?.right() ?: UserRepositoryErrors.RecordNotFound.left()
		} catch (e: Throwable) {
			UserRepositoryErrors.Unknown(e.message).left()
		}
	}

	override suspend fun createUser(newUser: NewUser): Either<UserRepositoryErrors.Unknown, EntityID<Int>> {
		return try {
			newSuspendedTransaction(Dispatchers.IO, db) {
				UserTable.insertAndGetId { user ->
					user[plexUsername] = newUser.plexUsername
					user[plexId] = newUser.plexId
					user[plexToken] = newUser.plexToken
					user[email] = newUser.email
					user[userType] = UserType.DEFAULT
				}
			}.right()
		} catch (e: Throwable) {
			UserRepositoryErrors.Unknown(e.message).left()
		}
	}
}