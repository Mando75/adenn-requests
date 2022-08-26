package routes

import components.layouts.DefaultLayout
import features.profile.routes.ProfileRouter
import features.requests.routes.RequestsRouter
import features.search.routes.SearchRouter
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
			element = SearchRouter.create()
		},
		jso {
			path = "/requests/*"
			element = RequestsRouter.create()
		},
		jso {
			path = "/profile/*"
			element = ProfileRouter.create()
		}
	)
})

