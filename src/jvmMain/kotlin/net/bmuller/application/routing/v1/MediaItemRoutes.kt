package net.bmuller.application.routing.v1

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/media-items")
class MediaItemResource {
	@kotlinx.serialization.Serializable
	@Resource("search")
	class Search(val parent: MediaItemResource = MediaItemResource(), val title: String? = "")

	@kotlinx.serialization.Serializable
	@Resource("{id}")
	class Id(val parent: MediaItemResource = MediaItemResource(), val id: Long) {
		@kotlinx.serialization.Serializable
		@Resource("providers")
		class Providers(val parent: Id)
	}

}


fun Route.mediaItems() {
	get<MediaItemResource.Search> { mediaItem ->
		call.respondText { "Search for ${mediaItem.title}" }
	}

	get<MediaItemResource.Id.Providers> { mediaItem ->
		call.respondText { "Providers for media item ${mediaItem.parent.id}" }
	}
}
