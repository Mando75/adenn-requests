package components.common

import hooks.useAuth
import react.FC
import react.PropsWithChildren
import react.router.Navigate


val RequireAuth = FC<PropsWithChildren> { props ->
	val auth = useAuth()

	if (auth.user == null) {
		Navigate {
			to = "/users/login"
			replace = true
		}
	} else {
		+props.children
	}
}