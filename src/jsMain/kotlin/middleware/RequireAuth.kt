package middleware

import hooks.useAuth
import react.FC
import react.PropsWithChildren
import react.router.Navigate


val RequireAuth = FC<PropsWithChildren>("RequireAuth") { props ->
	val (auth) = useAuth()

	if (auth.user == null) {
		Navigate {
			to = "/login"
			replace = true
		}
	} else {
		+props.children
	}
}