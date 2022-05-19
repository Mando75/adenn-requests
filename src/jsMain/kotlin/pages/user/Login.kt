package pages.user

import api.queries.usePlexLoginUrl
import components.config.SessionContext
import csstype.ClassName
import kotlinx.browser.window
import kotlinx.js.jso
import org.w3c.dom.HTMLButtonElement
import react.*
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main
import react.router.useNavigate

fun useCheckForSession() {
	val navigate = useNavigate()
	val session = useContext(SessionContext)
	useEffect(session, navigate) {
		session.user?.let { navigate("/", jso { replace = true }) }
	}
}


val Login = FC<Props>("Login") {
	useCheckForSession()
	val query = usePlexLoginUrl(window.location.host)
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