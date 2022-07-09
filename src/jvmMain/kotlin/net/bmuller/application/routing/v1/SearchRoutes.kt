package net.bmuller.application.routing.v1

import arrow.core.continuations.either
import http.SearchResource
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import net.bmuller.application.lib.error.DomainError
import net.bmuller.application.lib.respond
import net.bmuller.application.service.ISearchService

fun Route.search(searchService: ISearchService) {
	get<SearchResource.Multi> { context ->
		either<DomainError, Unit> {
			searchService.searchMulti(context.searchTerm)
		}.respond()
	}

	get<SearchResource.Movie> { context ->
		either<DomainError, Unit> {
			searchService.searchMovie(context.searchTerm).bind()
		}.respond()
	}

	get<SearchResource.TV> { context ->
		either<DomainError, Unit> {
			searchService.searchTV(context.searchTerm)
		}.respond()
	}
}