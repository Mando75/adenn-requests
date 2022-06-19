package routes

import components.layouts.DefaultLayout
import components.suspenseFallback.FullScreenFallback
import features.profile.routes.ProfileRoutes
import features.search.routes.SearchRoutes
import kotlinx.js.jso
import middleware.RequireAuth
import react.FC
import react.Props
import react.Suspense
import react.create
import react.dom.html.ReactHTML.div
import react.router.Outlet
import react.router.RouteObject

val PrivateApp = FC<Props>("PrivateApp") {
	RequireAuth {
		DefaultLayout {
			Suspense {
				fallback = FullScreenFallback.create()

				Outlet()
			}
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
			element = div.create() { +"Requests" }
		},
		jso {
			path = "/profile/*"
			element = ProfileRoutes.create()
		}
	)
})

