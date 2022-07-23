package http

import entities.RequestFilterMediaType
import entities.RequestFilters
import entities.RequestStatus
import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/requests")
class RequestResource(
	val status: List<RequestStatus>? = null,
	val searchTerm: String? = null,
	val mediaType: RequestFilterMediaType = RequestFilterMediaType.ALL,
	val page: Long? = 0
) {
	fun toRequestFilters(): RequestFilters =
		RequestFilters(status = status, searchTerm = searchTerm, mediaType = mediaType)
}