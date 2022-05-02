package net.bmuller.application.service

import arrow.core.Either
import net.bmuller.application.entities.PlexUser

class UserAuthService : BaseService() {

	suspend fun getPlexUser(authToken: String): Either<Throwable, PlexUser> {
		return plexTVRepository.getUser(authToken)
	}
}