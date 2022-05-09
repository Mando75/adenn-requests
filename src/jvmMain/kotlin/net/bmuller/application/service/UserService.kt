package net.bmuller.application.service

import arrow.core.Either

class UserService : BaseService() {
	suspend fun me(userId: Int?) = Either.catch {
		userId?.let { id -> userRepository.getUserById(id) }
	}
}