package features.dashboard.routes

import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val DashboardRouter = FC<Props>("DashboardRouter") {
	Routes {
		Route {
			index = true
			element = FC<Props> { +"Dashboard" }.create()

		}
	}
}