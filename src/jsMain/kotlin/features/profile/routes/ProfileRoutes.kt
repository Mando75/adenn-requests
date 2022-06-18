package features.profile.routes

import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val ProfileRoutes = FC<Props>("ProfileRoutes") {
	Routes {
		Route {
			index
			element = FC<Props> { +"User" }.create()
		}

		Route {
			path = "token"
			element = TokenPage.create()
		}

		Route {
			path = "logout"
			element = LogoutPage.create()
		}
	}
}