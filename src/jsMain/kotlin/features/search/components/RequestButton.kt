package features.search.components


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.span
import wrappers.DownloadIcon

external interface RequestButtonProps : Props {
	var onClick: () -> Unit
}

val RequestButton = FC<RequestButtonProps>("RequestButton") { props ->
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	button {
		className = ClassName(
			"""
			| bg-green-500 flex justify-around align-center px-6 py-2 rounded 
			| font-medium leading-tight uppercase text-white
			| hover:bg-green-600
			| focus:ring-1 focus:ring-green-700 focus:bg-green-600
			| transition duration-150 ease-in-out
		""".trimMargin()
		)
		onClick = {
			it.stopPropagation()
			props.onClick.invoke()
		}

		DownloadIcon {
			className = ClassName("text-white shrink h-6 w-6")
		}

		span {
			className = ClassName("grow")
			+"Request"
		}
	}
}