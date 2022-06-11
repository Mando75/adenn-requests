package pages

import context.SessionState
import context.useSession
import http.AuthResource
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import react.FC
import react.Props
import react.router.useNavigate
import react.useEffectOnce
import support.apiClient

val LogoutPage = FC<Props>("LogoutPage") {
	val navigate = useNavigate()
	val (_, setAuth) = useSession()
	useEffectOnce {
		MainScope().promise {
			apiClient.get(AuthResource.Logout())
		}
			.then { setAuth(SessionState(null)) }
			.then { navigate("/login") }
	}

	+"Loading..."
}