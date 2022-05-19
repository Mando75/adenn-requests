import csstype.ClassName
import hooks.useAuth
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.router.dom.Link


val Welcome = FC<Props>("Welcome") {

	val (auth) = useAuth()


	div {
		className = ClassName("bg-indigo-500")
		p {
			+"Hello ${auth.user?.plexUsername}"
		}
	}
	input {
		type = InputType.text
		value = name
		onChange = { event ->
			name = event.target.value
		}
	}
	Link {
		to = "/login"
		+"Login"
	}
}