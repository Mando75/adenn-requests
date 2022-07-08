package net.bmuller.application.service

import entities.Pagination
import net.bmuller.application.config.Env
import net.bmuller.application.repository.*
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.ceil
import kotlin.math.roundToLong

abstract class BaseService {
	private val pageSize = 25

	protected val env: Env by inject(Env::class.java)
	protected val plexAuthPinRepository: PlexAuthPinRepository by inject(PlexAuthPinRepository::class.java)
	protected val plexTVRepository: PlexTVRepository by inject(PlexTVRepository::class.java)
	protected val tmdbRepository: TMDBRepository by inject(TMDBRepository::class.java)
	protected val userRepository: UserRepository by inject(UserRepository::class.java)
	protected val requestsRepository: RequestsRepository by inject(RequestsRepository::class.java)

	protected fun calcOffset(pageNumber: Long): Pagination {
		val offset = pageNumber * pageSize
		return Pagination(offset = offset, limit = pageSize)
	}

	protected fun calcTotalPages(totalCount: Long): Long {
		return ceil(totalCount.div(pageSize.toDouble())).roundToLong()
	}
}