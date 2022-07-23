package net.bmuller.application.routing.api

import arrow.core.continuations.either
import entities.SearchResultEntity
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
			val searchResult = receiveCatching<SearchResultEntity>().bind()
			requestService.submitRequest(searchResult, session).bind()
		}.respond(HttpStatusCode.Created)
	}

	get<RequestResource> { context ->
		val filters = context.toRequestFilters()
		val page = context.page ?: 0

		requestService.getRequests(filters, page).respond()
	}
}