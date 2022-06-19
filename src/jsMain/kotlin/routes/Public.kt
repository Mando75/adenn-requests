package routes

import components.layouts.DefaultLayout
import components.suspenseFallback.FullScreenFallback
import features.auth.routes.AuthRoutes
import kotlinx.js.jso
import react.FC
import react.Props
import react.Suspense
import react.create
import react.router.Outlet
import react.router.RouteObject

val PublicApp = FC<Props>("PublicApp") {
	DefaultLayout {
		Suspense {
			fallback = FullScreenFallback.create()

			Outlet()
		}
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