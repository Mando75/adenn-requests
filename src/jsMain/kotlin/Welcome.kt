import csstype.ClassName
import entities.UserEntity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.router.dom.Link
import react.useEffectOnce
import react.useState
import support.getMe


private val scope = MainScope()

val Welcome = FC<Props> {

	var user by useState<UserEntity?>(null)

	useEffectOnce {
		scope.launch {
			user = getMe()
		}
	}

	div {
		className = ClassName("bg-indigo-500")
		p {
			+"Hello ${user?.plexUsername}"
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
		to = "/users/login"
		+"Login"
	}
}