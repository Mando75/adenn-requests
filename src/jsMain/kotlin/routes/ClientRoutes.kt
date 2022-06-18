package routes

import features.profile.routes.LogoutPage
import features.profile.routes.TokenPage
import layouts.DefaultLayout
import middleware.RequireAuth
import providers.useSession
import react.FC
import react.Fragment
import react.Props
import react.create
import react.router.Route
import react.router.Routes
import react.router.useRoutes

val AppRoutes = FC<Props> {
	val (auth) = useSession()

	val routes = auth.user?.let { PrivateRoutes } ?: PublicRoutes
	val element = useRoutes(routes.toTypedArray())

	Fragment {
		+element
	}
}

val ClientRoutes = FC<Props>("ClientRoutes") {
	Routes {
		Route {
			path = "/"
			element = RequireAuth.create { DefaultLayout() }

			Route {
				path = "requests"
				element = RequireAuth.create { +"Requests" }
			}

			Route {
				path = "user"
				element = FC<Props> { +"User" }.create()

				Route {
					path = "token"
					element = TokenPage.create()
				}
			}

			Route {
				path = "logout"
				element = LogoutPage.create()
			}
		}
	}
}