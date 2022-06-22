package routes

import components.layouts.DefaultLayout
import features.auth.routes.AuthRoutes
import kotlinx.js.jso
import react.FC
import react.Props
import react.create
import react.router.Outlet
import react.router.RouteObject

val PublicApp = FC<Props>("PublicApp") {
	DefaultLayout {
		Outlet()
	}
}


val PublicRoutes = listOf<RouteObject>(jso {
	path = "/"
	element = PublicApp.create()
	children = arrayOf(jso {
		path = "/auth/*"
		element = AuthRoutes.create()
	})
})