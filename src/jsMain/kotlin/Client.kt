import kotlinx.browser.document
import providers.InteractionProvider
import providers.SessionProvider
import react.FC
import react.Props
import react.StrictMode
import react.create
import react.dom.client.createRoot
import react.query.QueryClientProvider
import react.router.dom.BrowserRouter
import routes.AppRoutes
import utils.queryClient

private val App = FC<Props>("Root") {
	StrictMode {
		BrowserRouter {
			QueryClientProvider {
				client = queryClient

				SessionProvider {
					InteractionProvider {
						AppRoutes()
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