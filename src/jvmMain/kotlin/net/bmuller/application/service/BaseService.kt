package net.bmuller.application.service

import net.bmuller.application.repository.PlexAuthPinRepository
import net.bmuller.application.repository.PlexTVRepository
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject

@Suppress("unused")
abstract class BaseService {
	protected val plexAuthPinRepository: PlexAuthPinRepository by inject(PlexAuthPinRepository::class.java)
	protected val plexTVRepository: PlexTVRepository by inject(PlexTVRepository::class.java)
	protected val tmdbRepository: TMDBRepository by inject(TMDBRepository::class.java)
	protected val userRepository: UserRepository by inject(UserRepository::class.java)
}