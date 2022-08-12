package http

import entities.MediaType
import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/media/{type}/{id}")
class MediaResource(val type: MediaType, val id: Int) {
	@Serializable
	@Resource("providers")
	class Providers(val parent: MediaResource)
}