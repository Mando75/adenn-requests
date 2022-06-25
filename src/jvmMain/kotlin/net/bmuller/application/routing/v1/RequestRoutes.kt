package net.bmuller.application.routing.v1

import entities.SearchResult
import http.RequestResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.bmuller.application.plugins.inject
import net.bmuller.application.plugins.parseUserAuth
import net.bmuller.application.service.RequestService
import net.bmuller.application.service.RequestServiceErrors

fun Route.requests() {
	val requestService: RequestService by inject()

	post<RequestResource> {
		val session = call.parseUserAuth() ?: return@post call.respond(HttpStatusCode.Unauthorized)
		val searchResult: SearchResult = try {
			call.receive()
		} catch (e: Throwable) {
			return@post call.respond(HttpStatusCode.BadRequest, "Invalid search result in body")
		}

		requestService.submitRequest(searchResult, session)
			.map { request -> call.respond(HttpStatusCode.Created, request) }
			.mapLeft { error ->
				when (error) {
					is RequestServiceErrors.UserNotFound -> call.respond(HttpStatusCode.Forbidden, "User not found")
					is RequestServiceErrors.QuotaExceeded -> call.respond(
						HttpStatusCode.UnprocessableEntity,
						"Quota exceeded"
					)
				}
			}
	}

	get<RequestResource> { context ->
		requestService.getRequests(context.filters, context.page).map { requests ->
			call.respond(HttpStatusCode.OK, requests)
		}.mapLeft { e -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown Error") }
	}
}