package entities

import kotlinx.datetime.Instant
import lib.BackdropPath
import lib.PosterPath

@Suppress("unused")
@kotlinx.serialization.Serializable
enum class RequestStatus {
	REQUESTED,
	FULFILLED,
	REJECTED,
	WAITING,
	IMPORTED,
	DOWNLOADING
}

@kotlinx.serialization.Serializable
data class CreatedRequest(val id: Int)

typealias RequestList = PaginatedResponse<RequestListItem>

@kotlinx.serialization.Serializable
sealed class RequestListItem {
	abstract val id: Int
	abstract val tmdbId: Int
	abstract val title: String
	abstract val status: RequestStatus
	abstract val media: RequestMedia
	abstract val requestedAt: Instant
	abstract val modifiedAt: Instant
	abstract val requester: Requester

	@kotlinx.serialization.Serializable
	data class MovieRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val media: RequestMedia,
		override val requestedAt: Instant,
		override val modifiedAt: Instant,
		override val requester: Requester
	) : RequestListItem()

	@kotlinx.serialization.Serializable
	data class TVShowRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val media: RequestMedia,
		override val requestedAt: Instant,
		override val modifiedAt: Instant,
		override val requester: Requester
	) : RequestListItem()
}

@kotlinx.serialization.Serializable
data class RequestMedia(
	val backdropPath: BackdropPath? = null,
	val id: Int,
	val overview: String? = null,
	val posterPath: PosterPath,
	val releaseDate: String? = null,
	val title: String,
)

@kotlinx.serialization.Serializable
data class Requester(
	val id: Int,
	val username: String
)

@kotlinx.serialization.Serializable
enum class RequestFilterMediaType {
	MOVIE,
	TV,
	ALL
}

@kotlinx.serialization.Serializable
data class RequestFilters(
	val status: List<RequestStatus>? = null,
	val searchTerm: String? = null,
	val mediaType: RequestFilterMediaType = RequestFilterMediaType.ALL
)
