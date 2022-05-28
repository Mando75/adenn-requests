package components.config

import layouts.DefaultLayout
import middleware.RequireAuth
import pages.LoginPage
import pages.LogoutPage
import pages.SearchPage
import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val ClientRoutes = FC<Props>("ClientRoutes") {
	Routes {
		Route {
			path = "/"
			element = RequireAuth.create() { DefaultLayout() }

			Route {
				path = "search"
				element = SearchPage
			}

			Route {
				path = "requests"
				element = RequireAuth.create() { +"Requests" }
			}

			Route {
				path = "user"
				element = RequireAuth.create() { +"User" }
			}

			Route {
				path = "login"
				element = LoginPage.create()
			}

			Route {
				path = "logout"
				element = LogoutPage.create()
			}
		}

	}
}