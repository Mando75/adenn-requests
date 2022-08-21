package http

import entities.RequestFilterMediaType
import entities.RequestFilters
import entities.RequestStatus
import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/requests")
class RequestResource(
	val status: List<RequestStatus>? = null,
	val searchTerm: String? = null,
	val mediaType: RequestFilterMediaType = RequestFilterMediaType.ALL,
	val page: Long? = 0
) {

	@Serializable
	@Resource("{id}")
	class Id(val id: Int, @Suppress("unused") val parent: RequestResource = RequestResource()) {

		@Serializable
		@Resource("status")
		class Status(val parent: Id)
	}

	fun toRequestFilters(): RequestFilters =
		RequestFilters(status = status, searchTerm = searchTerm, mediaType = mediaType)
}