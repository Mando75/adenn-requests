package dto


@Suppress("unused")
enum class MediaItemType {
	MOVIE,
	TV
}

@Suppress("unused")
enum class MediaItemRequestStatus {
	CANCELLED,
	DOWNLOADING,
	FULFILLED,
	REJECTED,
	REQUESTED,
	WAITING
}

@Suppress("unused")
data class MediaItem(
	val backgroundUrl: String = "",
	val id: Int = 0,
	val key: String = "",
	val overview: String = "",
	val popularity: Double = 0.0,
	val posterUrl: String = "",
	val releaseDate: String = "",
	val request: MediaItemRequest = MediaItemRequest(),
	val title: String = "",
	val type: MediaItemType = MediaItemType.MOVIE,
	val url: String = ""
)

data class MediaItemRequest(
	val dateFulfilled: String = "",
	val dateCancelled: String = "",
	val dateRejected: String = "",
	val dateRequested: String = "",
	val rejectionReason: String = "",
	val requester: MediaItemRequester = MediaItemRequester(),
	val status: MediaItemRequestStatus = MediaItemRequestStatus.REQUESTED,
)

data class MediaItemRequester(val name: String = "", val email: String = "", val id: String = "")