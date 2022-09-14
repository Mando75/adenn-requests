package components.navigation


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.nav
import wrappers.Bars3Icon

external interface INavigationProps : Props {

}

val Navigation = FC<INavigationProps>("Navigation") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	nav {
		className = ClassName("")

		input {
			type = InputType.checkbox
			hidden = true
			id = "nav-open"
		}
		label {
			htmlFor = "nav-open"
			className =
				ClassName(
					"""absolute top-2 left-2 shadow-lg rounded-full 
						| p-2 bg-gray-100 text-gray-600 md:hidden w-8 h-8 
					""".trimMargin()
				)

			Bars3Icon()
		}

		Sidebar {
		}
	}
}