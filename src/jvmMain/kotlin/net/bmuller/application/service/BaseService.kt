package net.bmuller.application.service

import entities.Pagination
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.repository.*
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger

abstract class BaseService {
	private val pageSize = 25

	protected val logger: Logger by inject(Logger::class.java)
	protected val env: EnvironmentValues by inject(EnvironmentValues::class.java)
	protected val plexAuthPinRepository: PlexAuthPinRepository by inject(PlexAuthPinRepository::class.java)
	protected val plexTVRepository: PlexTVRepository by inject(PlexTVRepository::class.java)
	protected val tmdbRepository: TMDBRepository by inject(TMDBRepository::class.java)
	protected val userRepository: UserRepository by inject(UserRepository::class.java)
	protected val requestsRepository: RequestsRepository by inject(RequestsRepository::class.java)

	protected fun calcPagination(pageNumber: Long): Pagination {
		val offset = pageNumber * pageSize
		return Pagination(offset = offset, limit = pageSize)
	}
}