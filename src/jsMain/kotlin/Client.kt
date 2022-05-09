import kotlinx.browser.document
import react.create
import react.dom.render


fun main() {
	kotlinext.js.require("./app.css")
	val container = document.createElement("div")
	document.body!!.appendChild(container)

	val welcome = Welcome.create {
		name = "Kotlin/JS"
	}
	render(welcome, container)
}