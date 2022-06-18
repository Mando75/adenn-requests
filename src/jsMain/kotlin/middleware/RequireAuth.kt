package middleware

import kotlinx.js.jso
import providers.useSession
import react.FC
import react.PropsWithChildren
import react.router.useNavigate


val RequireAuth = FC<PropsWithChildren>("RequireAuth") { props ->
	val (auth) = useSession()
	val navigate = useNavigate()

	if (auth.loading) {
		+"Loading..."
	} else if (auth.user == null) {
		navigate("/login", jso { replace = true })
	} else {
		+props.children
	}
}