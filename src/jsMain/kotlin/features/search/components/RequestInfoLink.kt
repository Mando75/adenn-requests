package features.search.components


import csstype.ClassName
import entities.RequestStatus
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import utils.toColor

external interface RequestStatusProps : Props {
	var status: RequestStatus
}

val RequestInfoLink = FC<RequestStatusProps>("RequestStatus") { props ->
	// STATE
	val color = props.status.toColor()

	val text = when (props.status) {
		RequestStatus.REQUESTED -> "Requested"
		RequestStatus.FULFILLED -> "Already Added"
		RequestStatus.REJECTED -> "Request Rejected"
		RequestStatus.WAITING -> "Waiting for release"
		RequestStatus.IMPORTED -> "Searching for release"
		RequestStatus.DOWNLOADING -> "Downloading"
	}

	// HOOKS

	// EFFECTS

	// RENDER
	// TODO: make this a link
	div {
		className = ClassName(
			"""
			| ${color.allClassName()} flex justify-around align-center px-6 py-2 rounded
			| font-medium leading-tight uppercase text-white text-uppercase
			| transition duration-150 ease-in-out
		""".trimMargin()
		)
		+text
	}
}