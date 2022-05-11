import kotlinx.browser.document
import react.create
import react.dom.client.createRoot


fun main() {
	kotlinext.js.require("./app.css")
	val container = document.createElement("div")
	document.body!!.appendChild(container)

	val welcome = Welcome.create {
		name = "Kotlin/JS"
	}
	val root = createRoot(container)
	root.render(welcome)
}