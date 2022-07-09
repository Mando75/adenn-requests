package http

import entities.RequestFilters
import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/requests")
class RequestResource(val filters: RequestFilters? = RequestFilters(), val page: Long? = 0)