package net.bmuller.application.routing.v1

import http.SearchResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.service.ISearchService

fun Route.search(searchService: ISearchService) {
	get<SearchResource.Multi> { context ->
		searchService.searchMulti(context.searchTerm)
			.mapLeft { error ->
				call.respond(
					HttpStatusCode.InternalServerError,
					error.message ?: "An Unknown Error Occurred"
				)
			}
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}

	get<SearchResource.Movie> { context ->
		searchService.searchMovie(context.searchTerm)
			.mapLeft { error ->
				call.respond(
					HttpStatusCode.InternalServerError,
					error.message ?: "An Unknown Error Occurred"
				)
			}
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}

	get<SearchResource.TV> { context ->
		searchService.searchTV(context.searchTerm)
			.mapLeft { error ->
				call.respond(
					HttpStatusCode.InternalServerError,
					error.message ?: "An Unknown Error Occurred"
				)
			}
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}
}