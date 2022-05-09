import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.useState

external interface WelcomeProps : Props {
	var name: String
}

val Welcome = FC<WelcomeProps> { props ->
	var name by useState(props.name)
	console.log("Render")
	div {
		className = ClassName("bg-indigo-500")
		p {
			+"Hello $name"
		}
	}
	input {
		type = InputType.text
		value = name
		onChange = { event ->
			name = event.target.value
		}
	}
}