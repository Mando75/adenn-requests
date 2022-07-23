package features.requests.components


import csstype.ClassName
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.dd
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.dt

external interface RequestDetailLineItemProps : PropsWithChildren {
	var label: String
}

val RequestDetailLineItem = FC<RequestDetailLineItemProps>("RequestDetailLineItem") { props ->
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	div {
		className = ClassName("flex md:flex-row gap-1 md:items-center")
		dt {
			className = ClassName("font-bold text-sm md:text-xl")
			+props.label
		}
		dd {
			className = ClassName("text-sm md:text-base")
			+props.children
		}
	}
}