package features.dashboard.components


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2

external interface IOpenRequestsProps : Props {

}

val OpenRequests = FC<IOpenRequestsProps>("OpenRequests") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	h2 {
		className = ClassName("text-xl")
		+"Your open requests"
	}
}