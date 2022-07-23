package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import db.tables.RequestTable
import entities.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import lib.ImageTools
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.*
import net.bmuller.application.repository.RequestListData
import net.bmuller.application.repository.RequestsRepository
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.UserRepository
import java.time.Instant
import kotlin.time.Duration.Companion.days

interface IRequestService {
	suspend fun getRequests(
		filters: RequestFilters, page: Long
	): Either<DomainError, PaginatedResponse<RequestListItem>>

	suspend fun submitRequest(result: SearchResultEntity, session: UserSession): Either<DomainError, CreatedRequest>
}

fun requestService(
	requestsRepository: RequestsRepository, tmdbRepository: TMDBRepository, userRepository: UserRepository
) = object : IRequestService {
	override suspend fun getRequests(
		filters: RequestFilters, page: Long
	): Either<DomainError, RequestList> = coroutineScope {
		either {
			val pagination = calcOffset(page)
			val (requests, count) = requestsRepository.requests(filters, pagination).bind()
			val requestsWithMedia = requests.map { request ->
				async {
					when (request.mediaType) {
						RequestListData.MediaType.MOVIE -> constructMovieRequest(request).bind()
						RequestListData.MediaType.TV -> constructTVRequest(request).bind()
					}
				}
			}.awaitAll()

			return@either PaginatedResponse(
				items = requestsWithMedia, page = page, totalPages = calcTotalPages(count)
			)
		}
	}

	private suspend fun constructMovieRequest(request: RequestListData) =
		tmdbRepository.movieDetail(request.tmdbId).map { media ->
			RequestListItem.MovieRequest(
				id = request.id,
				tmdbId = request.tmdbId,
				title = request.title,
				status = request.status,
				requestedAt = request.createdAt.toKotlinInstant(),
				modifiedAt = request.modifiedAt.toKotlinInstant(),
				media = RequestMedia(
					id = media.id,
					overview = media.overview,
					posterPath = ImageTools.tmdbPosterPath(media.posterPath),
					releaseDate = media.releaseDate,
					title = media.title,
					backdropPath = media.backdropPath?.let { ImageTools.tmdbBackdropPath(media.backdropPath) }
				),
				requester = Requester(
					id = request.requester.id,
					username = request.requester.username,
					profilePicUrl = request.requester.profilePicUrl
				),
			)
		}


	private suspend fun constructTVRequest(request: RequestListData) =
		tmdbRepository.tvDetail(request.tmdbId).map { media ->
			RequestListItem.TVShowRequest(
				id = request.id,
				tmdbId = request.tmdbId,
				title = request.title,
				status = request.status,
				requestedAt = request.createdAt.toKotlinInstant(),
				modifiedAt = request.modifiedAt.toKotlinInstant(),
				media = RequestMedia(
					backdropPath = media.backdropPath?.let { ImageTools.tmdbBackdropPath(media.backdropPath) },
					id = media.id,
					overview = media.overview,
					posterPath = ImageTools.tmdbPosterPath(media.posterPath),
					releaseDate = media.firstAirDate,
					title = media.title
				),
				requester = Requester(
					id = request.requester.id,
					username = request.requester.username,
					profilePicUrl = request.requester.profilePicUrl
				),
			)
		}


	override suspend fun submitRequest(
		result: SearchResultEntity, session: UserSession
	): Either<DomainError, CreatedRequest> = either {
		val user = getUser(session.id).bind()
		checkRequestQuota(user, result is SearchResultEntity.MovieResult).bind()

		val mediaType = when (result) {
			is SearchResultEntity.MovieResult -> RequestTable.MediaType.MOVIE
			is SearchResultEntity.TVResult -> RequestTable.MediaType.TV
		}
		val (id) = requestsRepository.createRequest(result.title, result.id, mediaType, user.id).bind()

		CreatedRequest(id)
	}

	private suspend fun checkRequestQuota(
		user: UserEntity, isMovie: Boolean
	): Either<DomainError, Unit> = either {
		val limit = if (isMovie) user.movieQuotaLimit else user.tvQuotaLimit
		val days = if (isMovie) user.movieQuotaDays else user.tvQuotaDays
		val mediaType = if (isMovie) RequestTable.MediaType.MOVIE else RequestTable.MediaType.TV

		val timePeriod: Instant = Instant.now().minusSeconds(days.days.inWholeSeconds)

		val quotaUsage = requestsRepository.getQuotaUsage(user.id, timePeriod, mediaType).bind()

		quotaUsage.let { if (quotaUsage >= limit) QuotaExceeded.left() else Unit.right() }.bind()
	}

	private suspend fun getUser(userId: Int): Either<Forbidden, UserEntity> =
		userRepository.getUserById(userId).mapLeft { _ -> Forbidden("User $userId not found") }

	@Suppress("unused")
	private fun submitRequestToJobQueue(request: RequestListItem) {
		// TODO: submit request to job queue
		println("Submit request to job queue: ${request.id}")
	}
}

