package entities

@kotlinx.serialization.Serializable
data class Pagination(val offset: Long = 0, val limit: Int = 25)


@kotlinx.serialization.Serializable
data class PaginatedResponse<T>(val items: List<T>, val totalCount: Long, val offset: Long, val limit: Int)