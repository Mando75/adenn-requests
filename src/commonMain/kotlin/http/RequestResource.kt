package http

import entities.Pagination
import entities.RequestFilters
import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/requests")
class RequestResource(val filters: RequestFilters = RequestFilters(), val pagination: Pagination = Pagination())