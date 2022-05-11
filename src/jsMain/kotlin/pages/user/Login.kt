package pages.user

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main


val Login = FC<Props> {
	main {
		h1 {
			+"Login"
		}
		div {
			button {
				+"Click me to login"
			}
		}
	}
}