package features.auth.routes

import csstype.ClassName
import features.auth.api.usePlexLoginUrl
import kotlinx.browser.window
import kotlinx.js.jso
import org.w3c.dom.HTMLButtonElement
import providers.useSession
import react.FC
import react.Props
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import react.router.useNavigate
import react.useCallback
import react.useEffect

fun useCheckForSession() {
	val navigate = useNavigate()
	val (auth) = useSession()
	useEffect(auth, navigate) {
		auth.user?.let { navigate("/", jso { replace = true }) }
	}
}


val LoginPage = FC<Props>("LoginPage") {
	useCheckForSession()
	val query = usePlexLoginUrl(window.location.origin)
	val onLoginClick: MouseEventHandler<HTMLButtonElement> = useCallback(query.isLoading, query.isError, query.data) {
		query.refetch(null)
	}

	useEffect(query.data) {
		query.data?.let { data ->
			window.location.href = data.loginUrl
		}
	}

	main {
		h1 {
			className = ClassName("text-3xl")
			+"Login"
		}
		div {
			button {
				onClick = onLoginClick
				+"Login with Plex"
			}
		}
	}
}