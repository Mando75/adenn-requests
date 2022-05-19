package middleware

import hooks.useAuth
import react.FC
import react.PropsWithChildren
import react.router.Navigate


val RequireAuth = FC<PropsWithChildren>("RequireAuth") { props ->
	val (auth) = useAuth()

	auth.user?.let { +props.children } ?: Navigate {
		to = "/login"
		replace = true
	}
}