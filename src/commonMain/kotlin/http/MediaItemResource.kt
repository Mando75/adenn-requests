package http

import io.ktor.resources.*

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