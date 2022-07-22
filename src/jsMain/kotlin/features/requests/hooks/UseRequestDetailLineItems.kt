package features.requests.hooks

import components.tag.*
import entities.RequestListItem
import entities.RequestStatus
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.span

data class RequestDetailLineItem(
	val label: String,
	val child: ReactNode
)

fun useRequestDetailLineItems(request: RequestListItem): List<RequestDetailLineItem> {
	val status = createStatus(request)
	val requestedDate = requestedDate(request)

	return listOf(status, requestedDate)
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

private fun requestedDate(request: RequestListItem) = RequestDetailLineItem(
	label = "Requested: ",
	child = span.create {
		+request.id.toString()
	}
)