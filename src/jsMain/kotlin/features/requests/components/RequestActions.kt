package features.requests.components


import components.button.Button
import csstype.ClassName
import features.requests.hooks.useAllowedActions
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.key

external interface IRequestActionsProps : Props {
}

val RequestActions = FC<IRequestActionsProps>("RequestActions") { props ->
	/// HOOKS
	val allowedActions = useAllowedActions()
	/// STATE

	/// EFFECTS

	/// RENDER
	ul {
		allowedActions.map { action ->
			li {
				key = action.value.toString()
				className = ClassName("mt-2")

				Button {
					onClick = { println(action.value) }
					+action.label
				}
			}
		}
	}
}