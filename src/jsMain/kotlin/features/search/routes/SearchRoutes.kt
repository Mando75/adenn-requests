package features.search.routes

import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes


val SearchRoutes = FC<Props>("SearchRoutes") {
	Routes {
		Route {
			index = true
			element = SearchPage.create()
		}
	}
}