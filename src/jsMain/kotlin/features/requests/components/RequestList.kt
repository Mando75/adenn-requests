package features.requests.components


import csstype.ClassName
import entities.RequestListItem
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul

external interface RequestListProps : Props {
	var items: List<RequestListItem>
	var isLoading: Boolean
}

val RequestList = FC<RequestListProps>("RequestList") { props ->

	// STATE
	val isEmpty = !props.isLoading && props.items.isEmpty()

	// HOOKS

	// EFFECTS

	// RENDER
	if (isEmpty || props.isLoading) {
		div {
			className = ClassName("mt-64 w-full text-center text-2xl text-gray-400 rounded-sm")
			+(if (isEmpty) "No requests match the filter criteria" else "Loading...")
		}
	} else {
		ul {
			className = ClassName("grid grid-cols-1 gap-8")
			props.items.map { result -> RequestCard { request = result } }
		}
	}
}