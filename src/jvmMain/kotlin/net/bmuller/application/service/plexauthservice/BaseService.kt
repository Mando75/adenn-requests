package net.bmuller.application.service.plexauthservice

import net.bmuller.application.repository.PlexAuthPinRepository
import net.bmuller.application.repository.RequestsRepository
import net.bmuller.application.repository.TMDBRepository
import org.koin.java.KoinJavaComponent.inject

@Suppress("unused")
abstract class BaseService {
	protected val plexAuthPinRepository: PlexAuthPinRepository by inject(PlexAuthPinRepository::class.java)
	protected val requestsRepository: RequestsRepository by inject(RequestsRepository::class.java)
	protected val tmdbRepository: TMDBRepository by inject(TMDBRepository::class.java)
}