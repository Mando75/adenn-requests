import components.config.ClientRoutes
import context.InteractionProvider
import context.SessionProvider
import kotlinx.browser.document
import react.FC
import react.Props
import react.StrictMode
import react.create
import react.dom.client.createRoot
import react.query.QueryClientProvider
import react.router.dom.BrowserRouter
import support.queryClient

private val App = FC<Props> {
	StrictMode {
		BrowserRouter {
			QueryClientProvider {
				client = queryClient

				SessionProvider {
					InteractionProvider {
						ClientRoutes()
					}
				}
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