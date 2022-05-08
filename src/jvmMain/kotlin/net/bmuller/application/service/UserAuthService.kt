package net.bmuller.application.service

import entities.UserEntity
import net.bmuller.application.entities.PlexUser
import net.bmuller.application.repository.NewUser

class UserAuthService : BaseService() {

	suspend fun authFlow(authToken: String) {
		val plexUser = plexTVRepository.getUser(authToken)
		val existingUser = userRepository.getUserByPlexId(plexUserId = plexUser.id)
		val sessionUser: UserEntity = existingUser ?: registerNewUser(plexUser)
		createUserSession(sessionUser)
	}

	suspend fun registerNewUser(plexUser: PlexUser): UserEntity {
		val newUser = NewUser(
			plexUsername = plexUser.username,
			plexId = plexUser.id,
			plexToken = plexUser.authToken,
			email = plexUser.email,
		)
		return userRepository.createAndReturnUser(newUser)
	}

	fun createUserSession(user: UserEntity) {
		TODO("Not Implemented")
	}
}