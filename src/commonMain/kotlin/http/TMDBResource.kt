package http

import io.ktor.resources.*

@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/tmdb")
class TMDBResource {
	@kotlinx.serialization.Serializable
	@Resource("search")
	class Search(val parent: TMDBResource = TMDBResource()) {

		@kotlinx.serialization.Serializable
		@Resource("movies")
		class Movies(val parent: Search = Search(), val searchTerm: String? = "")

		@kotlinx.serialization.Serializable
		@Resource("tv")
		class TV(val parent: Search = Search(), val searchTerm: String? = "")

		@kotlinx.serialization.Serializable
		@Resource("multi")
		class Multi(val parent: Search = Search(), val searchTerm: String? = "")
	}
}