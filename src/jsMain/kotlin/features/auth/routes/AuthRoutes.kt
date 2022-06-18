package features.auth.routes

import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val AuthRoutes = FC<Props>("AuthRoutes") {
	Routes {
		Route {
			path = "/login"
			element = LoginPage.create()
		}
	}
}