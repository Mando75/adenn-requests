package features.requests.hooks

import components.tag.*
import entities.RequestListItem
import entities.RequestStatus
import react.ReactNode
import react.create
import utils.dateFormatter

data class RequestDetailLineItem(
	val label: String,
	val child: ReactNode
)

fun useRequestDetailLineItems(request: RequestListItem): List<RequestDetailLineItem> {
	val status = createStatus(request)
	val requested = requested(request)
	val updated = updated(request)

	return listOf(status, requested, updated)
}

private fun createStatus(request: RequestListItem): RequestDetailLineItem {
	val tagStyle = when (request.status) {
		RequestStatus.REQUESTED -> BlueTag()
		RequestStatus.FULFILLED -> GreenTag()
		RequestStatus.REJECTED -> RedTag()
		RequestStatus.WAITING -> PurpleTag()
		RequestStatus.IMPORTED -> BlueTag()
		RequestStatus.DOWNLOADING -> CyanTag()
	}

	return RequestDetailLineItem(
		label = "Status: ",
		child = Tag.create {
			style = tagStyle
			+request.status.name
		}
	)
}

private fun requested(request: RequestListItem) = RequestDetailLineItem(
	label = "Requested: ",
	child = ReactNode(dateFormatter(request.requestedAt))
)

private fun updated(request: RequestListItem) = RequestDetailLineItem(
	label = "Last Updated: ",
	child = ReactNode(dateFormatter(request.modifiedAt))
)