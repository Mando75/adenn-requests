package net.bmuller.application.service

import arrow.core.Either
import arrow.core.flatMap
import net.bmuller.application.repository.NewUser
import org.jetbrains.exposed.dao.id.EntityID

sealed class UserAuthServiceErrors {
	data class Unknown(val message: String?) : UserAuthServiceErrors()
}

class UserAuthService : BaseService() {

	suspend fun registerNewUser(authToken: String): Either<UserAuthServiceErrors.Unknown, EntityID<Int>> =
		plexTVRepository.getUser(authToken)
			.mapLeft { error -> UserAuthServiceErrors.Unknown(error.message) }
			.flatMap { plexUser ->
				val newUser = NewUser(
					plexUsername = plexUser.username,
					plexId = plexUser.id,
					plexToken = plexUser.authToken,
					email = plexUser.email,
				)
				userRepository.createUser(newUser)
					.mapLeft { error -> UserAuthServiceErrors.Unknown(error.message) }
			}
}