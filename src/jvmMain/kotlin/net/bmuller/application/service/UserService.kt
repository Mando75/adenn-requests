package net.bmuller.application.service

import arrow.core.Either
import entities.UserEntity
import net.bmuller.application.lib.error.DomainError
import net.bmuller.application.lib.error.EntityNotFound
import net.bmuller.application.repository.UserRepository

interface IUserService {
	suspend fun me(userId: Int): Either<DomainError, UserEntity>
}

fun userService(userRepository: UserRepository) = object : IUserService {
	override suspend fun me(userId: Int): Either<EntityNotFound, UserEntity> = userRepository.getUserById(userId)
}