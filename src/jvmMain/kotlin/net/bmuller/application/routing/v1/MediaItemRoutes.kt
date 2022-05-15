package net.bmuller.application.routing.v1

import http.MediaItemResource
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.mediaItems() {
	get<MediaItemResource.Search> { mediaItem ->
		call.respondText { "Search for ${mediaItem.title}" }
	}

	get<MediaItemResource.Id.Providers> { mediaItem ->
		call.respondText { "Providers for media item ${mediaItem.parent.id}" }
	}
}
