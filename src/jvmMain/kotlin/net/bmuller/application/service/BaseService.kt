package net.bmuller.application.service

import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.repository.PlexAuthPinRepository
import net.bmuller.application.repository.PlexTVRepository
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger

abstract class BaseService {
	protected val logger: Logger by inject(Logger::class.java)
	protected val env: EnvironmentValues by inject(EnvironmentValues::class.java)
	protected val plexAuthPinRepository: PlexAuthPinRepository by inject(PlexAuthPinRepository::class.java)
	protected val plexTVRepository: PlexTVRepository by inject(PlexTVRepository::class.java)
	protected val tmdbRepository: TMDBRepository by inject(TMDBRepository::class.java)
	protected val userRepository: UserRepository by inject(UserRepository::class.java)
}