package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import entities.*
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.*
import net.bmuller.application.repository.RequestsRepository
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.UserRepository
import java.time.Instant
import kotlin.time.Duration.Companion.days

interface IRequestService {
	suspend fun getRequests(filters: RequestFilters, page: Long): Either<DomainError, PaginatedResponse<RequestEntity>>
	suspend fun submitRequest(result: SearchResult, session: UserSession): Either<DomainError, RequestEntity>
}

fun requestService(
	requestsRepository: RequestsRepository,
	tmdbRepository: TMDBRepository,
	userRepository: UserRepository
) = object : IRequestService {
	override suspend fun getRequests(
		filters: RequestFilters,
		page: Long
	): Either<DomainError, PaginatedResponse<RequestEntity>> =
		either {
			val pagination = calcOffset(page)
			val (requests, count) = requestsRepository.requests(filters, pagination).bind()
			// TODO: Batch with something like a flow?
			requests.forEach { request ->
				when (request) {
					is RequestEntity.MovieRequest -> {
						val detail = tmdbRepository.movieDetail(request.tmdbId)
						println(detail)
					}
					is RequestEntity.TVShowRequest -> {
						val detail = tmdbRepository.tvDetail(request.tmdbId)
						println(detail)
					}
				}
			}
			return@either PaginatedResponse(
				items = requests,
				page = page,
				totalPages = calcTotalPages(count)
			)
		}

	override suspend fun submitRequest(
		result: SearchResult,
		session: UserSession
	): Either<DomainError, RequestEntity> = either {
		val user = getUser(session.id).bind()
		checkRequestQuota(user, result is SearchResult.MovieResult).bind()

		val newRequest = RequestEntity.fromSearchResult(result)
		val request = requestsRepository.createAndReturnRequest(newRequest, user, true).bind()
		submitRequestToJobQueue(request)
		return@either request
	}

	private suspend fun checkRequestQuota(
		user: UserEntity,
		isMovie: Boolean
	): Either<DomainError, Unit> = either {
		val limit = if (isMovie) user.movieQuotaLimit else user.tvQuotaLimit
		val days = if (isMovie) user.movieQuotaDays else user.tvQuotaDays

		val timePeriod: Instant = Instant.now().minusSeconds(days.days.inWholeSeconds)

		val quotaUsage = requestsRepository.getQuotaUsage(user.id, timePeriod, isMovie).bind()

		quotaUsage.let { if (quotaUsage >= limit) QuotaExceeded.left() else Unit.right() }.bind()
	}

	private suspend fun getUser(userId: Int): Either<Forbidden, UserEntity> =
		userRepository.getUserById(userId)
			.mapLeft { _ -> Forbidden("User $userId not found") }

	private fun submitRequestToJobQueue(@Suppress("unused") request: RequestEntity) {
		// TODO: submit request to job queue
	}
}
