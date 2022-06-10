package components.config

import layouts.DefaultLayout
import middleware.RequireAuth
import pages.LoginPage
import pages.LogoutPage
import pages.SearchPage
import pages.user.TokenPage
import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val ClientRoutes = FC<Props>("ClientRoutes") {
	Routes {
		Route {
			path = "/"
			element = RequireAuth.create { DefaultLayout() }

			Route {
				path = "search"
				element = SearchPage.create()
			}

			Route {
				path = "requests"
				element = RequireAuth.create { +"Requests" }
			}

			Route {
				path = "user"
				element = TokenPage.create()

				Route {
					path = "token"
					element = TokenPage.create()
				}
			}

			Route {
				path = "logout"
				element = LogoutPage.create()
			}
		}

		Route {
			path = "/login"
			element = LoginPage.create()
		}
	}
}