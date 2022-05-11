import components.ClientRoutes
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import react.router.dom.BrowserRouter


fun main() {
	kotlinext.js.require("./app.css")
	val container = document.createElement("div")
	document.body!!.appendChild(container)

	val router = BrowserRouter.create {
		ClientRoutes()
	}

	val root = createRoot(container)
	root.render(router)
}