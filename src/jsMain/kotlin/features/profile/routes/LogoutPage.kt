package features.profile.routes

import http.AuthResource
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import lib.ApiClient.apiClient
import providers.SessionState
import providers.useSession
import react.FC
import react.Props
import react.router.useNavigate
import react.useEffectOnce

val LogoutPage = FC<Props>("LogoutPage") {
	val navigate = useNavigate()
	val (_, setAuth) = useSession()
	useEffectOnce {
		MainScope().promise {
			apiClient.get(AuthResource.Logout())
		}
			.then { setAuth(SessionState(null, false)) }
			.then { navigate("/login") }
	}

	+"Logging you out..."
}