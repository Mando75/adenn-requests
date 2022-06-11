package middleware

import context.useSession
import react.FC
import react.PropsWithChildren
import react.router.Navigate


val RequireAuth = FC<PropsWithChildren>("RequireAuth") { props ->
	val (auth) = useSession()

	auth.user?.let { +props.children } ?: Navigate {
		to = "/login"
		replace = true
	}
}