package pages.user

import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.main


val Login = FC<Props> {
	main {
		h1 {
			+"Login"
		}
		div {
			Button {
				variant = ButtonVariant.contained
				+"Login with Plex"
			}
		}
	}
}