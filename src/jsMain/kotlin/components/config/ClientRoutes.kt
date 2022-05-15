package components.config

import Welcome
import components.common.RequireAuth
import pages.user.Login
import pages.user.Logout
import react.FC
import react.Props
import react.create
import react.router.Route
import react.router.Routes

val ClientRoutes = FC<Props> {
	console.log("Mounted")
	Routes {
		Route {
			path = "/"
			element = RequireAuth.create() { Welcome() }
		}
		Route {
			path = "users"

			Route {
				path = "login"
				element = Login.create()
			}

			Route {
				path = "logout"
				element = Logout.create()
			}
		}
	}
}