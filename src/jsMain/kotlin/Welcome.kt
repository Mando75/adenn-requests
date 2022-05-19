import components.config.SessionContext
import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.router.dom.Link
import react.useContext


val Welcome = FC<Props>("Welcome") {

	val context = useContext(SessionContext)


	div {
		className = ClassName("bg-indigo-500")
		p {
			+"Hello ${context.user?.plexUsername}"
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
		to = "/user/login"
		+"Login"
	}
}