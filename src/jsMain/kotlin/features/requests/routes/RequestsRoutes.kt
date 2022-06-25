package features.requests.routes


import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val RequestsRoutes = FC<Props>("RequestsRoutes") {
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	Routes {
		Route {
			index = true
			element = RequestsPage.create()
		}
	}
}