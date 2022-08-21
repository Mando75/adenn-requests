package entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import lib.BackdropPath
import lib.PosterPath

@Suppress("unused")
@Serializable
enum class RequestStatus {
	REQUESTED,
	FULFILLED,
	REJECTED,
	WAITING,
	IMPORTED,
	DOWNLOADING
}

@Serializable
data class CreatedRequest(val id: Int)

typealias RequestList = PaginatedResponse<RequestEntity>

@Serializable
sealed class RequestEntity {
	abstract val id: Int
	abstract val tmdbId: Int
	abstract val title: String
	abstract val status: RequestStatus
	abstract val media: RequestMedia
	abstract val requestedAt: Instant
	abstract val modifiedAt: Instant
	abstract val requester: Requester

	@Serializable
	data class MovieRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val media: RequestMedia,
		override val requestedAt: Instant,
		override val modifiedAt: Instant,
		override val requester: Requester
	) : RequestEntity()

	@Serializable
	data class TVShowRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val media: RequestMedia,
		override val requestedAt: Instant,
		override val modifiedAt: Instant,
		override val requester: Requester
	) : RequestEntity()
}

@Serializable
data class RequestMedia(
	val backdropPath: BackdropPath? = null,
	val id: Int,
	val overview: String? = null,
	val posterPath: PosterPath,
	val releaseDate: String? = null,
	val title: String,
)

@Serializable
data class Requester(
	val id: Int,
	val username: String,
	val profilePicUrl: String?
)

@Serializable
enum class RequestFilterMediaType {
	MOVIE,
	TV,
	ALL
}

@Serializable
data class RequestFilters(
	val status: List<RequestStatus>? = null,
	val searchTerm: String? = null,
	val mediaType: RequestFilterMediaType = RequestFilterMediaType.ALL
)

@Serializable
data class UpdateRequestStatus(
	val status: RequestStatus,
	val rejectionReason: String?
)
