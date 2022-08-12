package http

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/auth")
class AuthResource {
	@kotlinx.serialization.Serializable
	@Resource("plex")
	class Plex(@Suppress("unused") val parent: AuthResource = AuthResource()) {

		@kotlinx.serialization.Serializable
		@Resource("login-url")
		class LoginUrl(@Suppress("unused") val parent: Plex = Plex(), val forwardHost: String)

		@kotlinx.serialization.Serializable
		@Resource("callback")
		class Callback(@Suppress("unused") val parent: Plex = Plex(), val pinId: String)
	}

	@kotlinx.serialization.Serializable
	@Resource("logout")
	class Logout(@Suppress("unused") val parent: AuthResource = AuthResource())

	@kotlinx.serialization.Serializable
	@Resource("token")
	class Token(val parent: AuthResource = AuthResource())
}