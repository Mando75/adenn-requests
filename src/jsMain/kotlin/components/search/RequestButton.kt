package components.search


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.span
import wrappers.DownloadIcon

val RequestButton = FC<Props>("RequestButton") {
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
		onClick = { it.stopPropagation() }

		DownloadIcon {
			className = ClassName("text-white flex-shrink h-6 w-6")
		}

		span {
			className = ClassName("flex-grow")
			+"Request"
		}
	}
}