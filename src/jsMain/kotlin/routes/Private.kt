package routes

import components.layouts.DefaultLayout
import features.profile.routes.ProfileRoutes
import features.requests.routes.RequestsRoutes
import features.search.routes.SearchRoutes
import kotlinx.js.jso
import middleware.RequireAuth
import react.FC
import react.Props
import react.create
import react.router.Outlet
import react.router.RouteObject

val PrivateApp = FC<Props>("PrivateApp") {
	RequireAuth {
		DefaultLayout {
			Outlet()
		}
	}
}

val PrivateRoutes: List<RouteObject> = listOf(jso {
	path = "/"
	element = PrivateApp.create()
	children = arrayOf(
		jso {
			path = "/search/*"
			element = SearchRoutes.create()
		},
		jso {
			path = "/requests/*"
			element = RequestsRoutes.create()
		},
		jso {
			path = "/profile/*"
			element = ProfileRoutes.create()
		}
	)
})

