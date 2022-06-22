package features.search.components


import csstype.ClassName
import entities.RequestStatus
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface RequestStatusProps : Props {
	var status: RequestStatus
}

val RequestInfoLink = FC<RequestStatusProps>("RequestStatus") { props ->
	// STATE
	val color = when (props.status) {
		RequestStatus.REQUESTED -> "bg-blue"
		RequestStatus.FULFILLED -> "bg-green"
		RequestStatus.REJECTED -> "bg-red"
		RequestStatus.WAITING -> "bg-purple"
		RequestStatus.IMPORTED -> "bg-blue"
		RequestStatus.DOWNLOADING -> "bg-cyan"
	}

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
			| $color-500 flex justify-around align-center px-6 py-2 rounded
			| font-medium leading-tight uppercase text-white text-uppercase
			| hover:$color-600
			| transition duration-150 ease-in-out
		""".trimMargin()
		)
		+text
	}
}