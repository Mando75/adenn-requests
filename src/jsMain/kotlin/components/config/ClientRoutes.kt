package components.config

import Welcome
import layouts.DefaultLayout
import middleware.RequireAuth
import pages.user.Login
import pages.user.Logout
import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val ClientRoutes = FC<Props>("ClientRoutes") {
	Routes {
		Route {
			path = "/"
			element = RequireAuth.create() { DefaultLayout { Welcome() } }
		}
		Route {
			path = "search"
			element = RequireAuth.create() { DefaultLayout { +"Search" } }
		}
		Route {
			path = "requests"
			element = RequireAuth.create() { DefaultLayout { +"Requests" } }
		}
		Route {
			path = "user"
			element = RequireAuth.create() { DefaultLayout { +"User" } }
		}

		Route {
			path = "login"
			element = DefaultLayout.create() { Login() }
		}

		Route {
			path = "logout"
			element = DefaultLayout.create() { Logout() }
		}
	}
}