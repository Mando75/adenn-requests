package components.tag


import csstype.ClassName
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.span

external interface TagProps : PropsWithChildren {
	var className: ClassName
}

val Tag = FC<TagProps>("Tag") { props ->
	/// RENDER
	span {
		className =
			ClassName("text-white text-sm font-medium rounded-full px-2 py-1 ${props.className}")
		+props.children
	}
}