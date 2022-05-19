package http

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/users")
class UserResource {

	@kotlinx.serialization.Serializable
	@Resource("me")
	class Me(@Suppress("unused") val parent: UserResource = UserResource())
}