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
		className = ClassName("flex gap-1")
		dt {
			className = ClassName("font-bold text-xl")
			+props.label
		}
		dd {
			className = ClassName("mt-1")
			+props.children
		}
	}
}