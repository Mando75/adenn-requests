package http

import io.ktor.resources.*

@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/search")
class SearchResource {

	@kotlinx.serialization.Serializable
	@Resource("multi")
	class Multi(val parent: SearchResource = SearchResource(), val searchTerm: String)

	@kotlinx.serialization.Serializable
	@Resource("movie")
	class Movie(val parent: SearchResource = SearchResource(), val searchTerm: String)

	@kotlinx.serialization.Serializable
	@Resource("tv")
	class TV(val parent: SearchResource = SearchResource(), val searchTerm: String)

}