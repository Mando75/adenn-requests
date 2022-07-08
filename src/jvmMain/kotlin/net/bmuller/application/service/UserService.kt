package net.bmuller.application.service

import arrow.core.Either
import entities.UserEntity
import net.bmuller.application.repository.UserRepository

interface IUserService {
	suspend fun me(userId: Int?): Either<Throwable, UserEntity?>
}

fun userService(userRepository: UserRepository) = object : IUserService {
	override suspend fun me(userId: Int?) = Either.catch {
		userId?.let { id -> userRepository.getUserById(id) }
	}
}

class UserService : BaseService() {
	suspend fun me(userId: Int?) = Either.catch {
		userId?.let { id -> userRepository.getUserById(id) }
	}
}