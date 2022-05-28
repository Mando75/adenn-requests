package net.bmuller.application.routing.v1

import http.TMDBResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.plugins.inject
import net.bmuller.application.repository.TMDBRepository

fun Route.tmdb() {
	val tmdbRepository: TMDBRepository by inject()

	get<TMDBResource.Search.Movies> { context ->
		val searchTerm = context.searchTerm ?: ""
		tmdbRepository.searchMovies(searchTerm)
			.mapLeft { error -> call.respond(HttpStatusCode.InternalServerError, error.toString()) }
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}

	get<TMDBResource.Search.TV> { context ->
		val searchTerm = context.searchTerm ?: ""
		tmdbRepository.searchTVShows(searchTerm)
			.mapLeft { error -> call.respond(HttpStatusCode.InternalServerError, error.toString()) }
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}

	get<TMDBResource.Search.Multi> { context ->
		val searchTerm = context.searchTerm ?: ""
		tmdbRepository.searchMulti(searchTerm)
			.mapLeft { error -> call.respond(HttpStatusCode.InternalServerError, error.toString()) }
			.map { results -> call.respond(HttpStatusCode.OK, results) }
	}
}