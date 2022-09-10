package http

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/auth")
class AuthResource {
	@Serializable
	@Resource("plex")
	class Plex(@Suppress("unused") val parent: AuthResource = AuthResource()) {

		@Serializable
		@Resource("login-url")
		class LoginUrl(@Suppress("unused") val parent: Plex = Plex(), val forwardHost: String)

		@Serializable
		@Resource("callback")
		class Callback(@Suppress("unused") val parent: Plex = Plex(), val pinId: String)
	}

	@Serializable
	@Resource("init")
	class Initialize(@Suppress("unused") val parent: AuthResource = AuthResource()) {
		@Serializable
		@Resource("login-url")
		class LoginUrl(@Suppress("unused") val parent: Initialize = Initialize(), val forwardHost: String)

		@Serializable
		@Resource("callback")
		class Callback(@Suppress("unused") val parent: Initialize = Initialize(), val pinId: String)
	}

	@Serializable
	@Resource("logout")
	class Logout(@Suppress("unused") val parent: AuthResource = AuthResource())

	@Serializable
	@Resource("token")
	class Token(val parent: AuthResource = AuthResource())
}