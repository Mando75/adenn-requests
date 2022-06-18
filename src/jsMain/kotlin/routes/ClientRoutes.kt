package routes

import providers.useSession
import react.FC
import react.Fragment
import react.Props
import react.router.useRoutes

val AppRoutes = FC<Props>("AppRoutes") {
	val (auth) = useSession()

	val routes = auth.user?.let { PrivateRoutes } ?: PublicRoutes
	val element = useRoutes(routes.toTypedArray())

	Fragment {
		+element
	}
}
