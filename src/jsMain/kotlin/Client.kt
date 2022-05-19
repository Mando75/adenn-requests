import components.config.ClientRoutes
import components.config.SessionManager
import kotlinx.browser.document
import kotlinx.js.jso
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.query.DefaultOptions
import react.query.QueryClient
import react.query.QueryClientProvider
import react.query.QueryObserverOptions
import react.router.dom.BrowserRouter

val queryClientQueryOptions: QueryObserverOptions<*, Error, *, *, *> = jso {
	refetchOnWindowFocus = false
}

val defaultQueryOptions: DefaultOptions<Error> = jso {
	queries = queryClientQueryOptions
}

private val queryClient = QueryClient(jso { defaultOptions = defaultQueryOptions })

private val App = FC<Props> {
	BrowserRouter {
		QueryClientProvider {
			client = queryClient
			SessionManager {
				ClientRoutes()
			}
		}
	}
}

fun main() {
	kotlinext.js.require("./app.css")
	val container = document.createElement("div")
	document.body!!.appendChild(container)

	val root = createRoot(container)
	root.render(App.create())
}