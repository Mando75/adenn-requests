package net.bmuller.application.service

import arrow.core.Either
import entities.UserEntity
import net.bmuller.application.lib.DomainError
import net.bmuller.application.repository.UserRepository

interface UserService {
	suspend fun me(userId: Int): Either<DomainError, UserEntity>
}

fun userService(userRepository: UserRepository) = object : UserService {
	override suspend fun me(userId: Int): Either<DomainError, UserEntity> = userRepository.getUserById(userId)
}