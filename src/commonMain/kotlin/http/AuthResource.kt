package http

import io.ktor.resources.*

@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/auth")
class AuthResource {
	@kotlinx.serialization.Serializable
	@Resource("plex")
	class Plex(val parent: AuthResource = AuthResource()) {

		@kotlinx.serialization.Serializable
		@Resource("login-url")
		class LoginUrl(val parent: Plex = Plex(), val forwardHost: String)

		@kotlinx.serialization.Serializable
		@Resource("callback")
		class Callback(val parent: Plex = Plex(), val pinId: Long)
	}

	@kotlinx.serialization.Serializable
	@Resource("logout")
	class Logout(val parent: AuthResource = AuthResource())

	@kotlinx.serialization.Serializable
	@Resource("token")
	class Token(val parent: AuthResource = AuthResource())
}