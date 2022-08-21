package net.bmuller.application.routing.api

import http.MediaResource
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import net.bmuller.application.lib.respond
import net.bmuller.application.service.MediaService

fun Route.media(mediaService: MediaService) {
	get<MediaResource> { context ->
		mediaService.getMediaDetail(id = context.id, type = context.type).respond()
	}

	get<MediaResource.Providers> { context ->
		mediaService.getMediaProviders(id = context.parent.id, type = context.parent.type).respond()
	}
}