package http

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/search")
class SearchResource {

	@kotlinx.serialization.Serializable
	@Resource("multi")
	class Multi(@Suppress("unused") val parent: SearchResource = SearchResource(), val searchTerm: String)

	@kotlinx.serialization.Serializable
	@Resource("movie")
	class Movie(@Suppress("unused") val parent: SearchResource = SearchResource(), val searchTerm: String)

	@kotlinx.serialization.Serializable
	@Resource("tv")
	class TV(@Suppress("unused") val parent: SearchResource = SearchResource(), val searchTerm: String)

}