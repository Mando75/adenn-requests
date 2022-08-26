package features.search.components


import components.button.Button
import csstype.ClassName
import entities.RequestStatus
import react.FC
import react.Props
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
	Button {
		disabled = true
		className = ClassName("w-full ${color.allClassName()}")
		+text
	}
}