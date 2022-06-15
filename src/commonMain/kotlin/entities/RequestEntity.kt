package entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

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
sealed class RequestEntity {
	abstract val id: Int
	abstract val tmdbId: Int
	abstract val title: String
	abstract val posterPath: String
	abstract val releaseDate: Instant?
	abstract val status: RequestStatus
	abstract val requester: UserEntity?
	abstract val rejectionReason: String?
	abstract val dateFulfilled: Instant?
	abstract val dateRejected: Instant?
	abstract val createdAt: Instant
	abstract val modifiedAt: Instant

	@kotlinx.serialization.Serializable
	data class MovieRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val posterPath: String,
		override val releaseDate: Instant? = null,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val requester: UserEntity?,
		override val rejectionReason: String? = null,
		override val dateFulfilled: Instant? = null,
		override val dateRejected: Instant? = null,
		override val createdAt: Instant = Clock.System.now(),
		override val modifiedAt: Instant = Clock.System.now()
	) : RequestEntity()

	@kotlinx.serialization.Serializable
	data class TVShowRequest(
		override val id: Int = 0,
		override val tmdbId: Int,
		override val title: String,
		override val posterPath: String,
		override val releaseDate: Instant? = null,
		override val status: RequestStatus = RequestStatus.REQUESTED,
		override val requester: UserEntity?,
		override val rejectionReason: String? = null,
		override val dateFulfilled: Instant? = null,
		override val dateRejected: Instant? = null,
		override val createdAt: Instant = Clock.System.now(),
		override val modifiedAt: Instant = Clock.System.now()
	) : RequestEntity()
}
