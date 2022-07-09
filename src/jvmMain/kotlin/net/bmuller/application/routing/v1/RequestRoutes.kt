package net.bmuller.application.routing.v1

import arrow.core.continuations.either
import entities.RequestFilters
import entities.SearchResult
import http.RequestResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import net.bmuller.application.lib.receiveCatching
import net.bmuller.application.lib.respond
import net.bmuller.application.plugins.parseUserAuth
import net.bmuller.application.service.IRequestService

fun Route.requests(requestService: IRequestService) {

	post<RequestResource> {
		either {
			val session = call.parseUserAuth().bind()
			val searchResult = receiveCatching<SearchResult>().bind()
			requestService.submitRequest(searchResult, session).bind()
		}.respond(HttpStatusCode.Created)
	}

	get<RequestResource> { context ->
		val filters = context.filters ?: RequestFilters()
		val page = context.page ?: 0
		requestService.getRequests(filters, page).respond()
	}
}