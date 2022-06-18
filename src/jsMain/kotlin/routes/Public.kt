package routes

import features.auth.routes.AuthRoutes
import kotlinx.js.jso
import react.create
import react.router.RouteObject


val PublicRoutes = listOf<RouteObject>(
	jso {
		path = "/auth/*"
		element = AuthRoutes.create()
	}
)