package net.bmuller.application.routing.v1

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.plugins.inject
import net.bmuller.application.repository.TMDBRepository

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
	}
}

fun Route.tmdb() {
	val tmdbRepository: TMDBRepository by inject()

	get<TMDBResource.Search.Movies> { search ->
		val searchTerm = search.searchTerm ?: ""
		tmdbRepository.searchMovies(searchTerm).mapLeft { error ->
			call.respond(HttpStatusCode.InternalServerError, error.toString())
		}.map { results ->
			call.respond(HttpStatusCode.OK, results)
		}
	}
}