package features.search.components


import components.button.Button
import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.span

external interface RequestButtonProps : Props {
	var onClick: () -> Unit
}

val SearchResultDetailButton = FC<RequestButtonProps>("RequestButton") { props ->

	/// RENDER
	Button {
		className = ClassName(
			"""
			| bg-green-500 
			| hover:bg-green-600 
			| focus:ring-1 focus:ring-green-700 focus:bg-green-600 
		""".trimMargin()
		)
		onClick = {
			props.onClick()
		}

		span {
			className = ClassName("grow")
			+"Details"
		}
	}
}