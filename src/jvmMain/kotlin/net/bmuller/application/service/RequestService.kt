package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import entities.*
import net.bmuller.application.entities.UserSession
import java.time.Instant
import kotlin.time.Duration.Companion.days

sealed class RequestServiceErrors {
	object UserNotFound : RequestServiceErrors()
	object QuotaExceeded : RequestServiceErrors()
}

class RequestService : BaseService() {

	suspend fun getRequests(filters: RequestFilters, page: Long): Either<Throwable, PaginatedResponse<RequestEntity>> =
		Either.catch {
			val pagination = calcOffset(page)
			val (requests, count) = requestsRepository.requests(filters, pagination)
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
			return@catch PaginatedResponse(
				items = requests,
				page = page,
				totalPages = calcTotalPages(count)
			)
		}

	suspend fun submitRequest(result: SearchResult, session: UserSession) = either {
		val user = getUser(session.id).bind()
		checkRequestQuota(user, result is SearchResult.MovieResult).bind()

		val newRequest = RequestEntity.fromSearchResult(result)
		val request = requestsRepository.createAndReturnRequest(newRequest, user, true)
		submitRequestToJobQueue(request)
		return@either request
	}

	private suspend fun checkRequestQuota(
		user: UserEntity,
		isMovie: Boolean
	): Either<RequestServiceErrors.QuotaExceeded, Unit> {
		val limit = if (isMovie) user.movieQuotaLimit else user.tvQuotaLimit
		val days = if (isMovie) user.movieQuotaDays else user.tvQuotaDays

		val timePeriod: Instant = Instant.now().minusSeconds(days.days.inWholeSeconds)

		val quotaUsage = requestsRepository.getQuotaUsage(user.id, timePeriod, isMovie)

		if (quotaUsage >= limit) {
			return RequestServiceErrors.QuotaExceeded.left()
		}

		return Unit.right()
	}

	private suspend fun getUser(userId: Int): Either<RequestServiceErrors.UserNotFound, UserEntity> {
		return userRepository.getUserById(userId)?.right() ?: RequestServiceErrors.UserNotFound.left()
	}

	private fun submitRequestToJobQueue(request: RequestEntity) {
		// TODO: submit request to job queue
	}
}