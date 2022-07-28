package net.bmuller.application.routing.api

import http.SearchResource
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import net.bmuller.application.lib.respond
import net.bmuller.application.service.SearchService

fun Route.search(searchService: SearchService) {
	get<SearchResource.Multi> { context ->
		searchService.searchMulti(context.searchTerm).respond()
	}

	get<SearchResource.Movie> { context ->
		searchService.searchMovie(context.searchTerm).respond()
	}

	get<SearchResource.TV> { context ->
		searchService.searchTV(context.searchTerm).respond()
	}
}