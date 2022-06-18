package routes

import components.spinners.BarsScaleMiddle
import csstype.ClassName
import features.profile.routes.ProfileRoutes
import features.search.routes.SearchRoutes
import kotlinx.js.jso
import layouts.DefaultLayout
import react.FC
import react.Props
import react.Suspense
import react.create
import react.dom.html.ReactHTML.div
import react.router.Outlet
import react.router.RouteObject

val Fallback = FC<Props>("SuspenseFallback") {
	div {
		className = ClassName("h-full, w-full flex items-center justify-center")
		BarsScaleMiddle()
	}
}

val App = FC<Props>("App") {
	DefaultLayout {
		Suspense {
			fallback = Fallback.create()

			Outlet()
		}
	}
}

val PrivateRoutes: List<RouteObject> = listOf(jso {
	path = "/"
	element = App.create()
	children = arrayOf(
		jso {
			path = "/search"
			element = SearchRoutes.create()
		},
		jso {
			path = "/requests"
			element = div.create() { +"Requests" }
		},
		jso {
			path = "/profile"
			element = ProfileRoutes.create()
		}
	)
})

