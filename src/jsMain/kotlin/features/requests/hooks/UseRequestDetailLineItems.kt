package features.requests.hooks

import components.tag.*
import csstype.ClassName
import entities.RequestListItem
import entities.RequestStatus
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.span
import utils.dateFormatter

data class RequestDetailLineItem(
	val label: String, val child: ReactNode
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

	return RequestDetailLineItem(label = "Status ", child = Tag.create {
		style = tagStyle
		+request.status.name
	})
}

private fun requested(request: RequestListItem): RequestDetailLineItem {
	val date = dateFormatter(request.requestedAt)

	return RequestDetailLineItem(label = "Requested", child = div.create {
		className = ClassName("flex items-center")

		+"on $date"
		span {
			className = ClassName("hidden md:inline ml-1")
			+" by"
		}
		request.requester.profilePicUrl?.let {
			img {
				className = ClassName("rounded-full w-12 ml-2 hidden md:block")
				src = request.requester.profilePicUrl
				alt = "${request.requester.username} profile picture"
			}
		}
		span {
			className = ClassName("font-bold font-lg ml-1 hidden md:inline")
			+request.requester.username
		}
	})
}

private fun updated(request: RequestListItem) = RequestDetailLineItem(
	label = "Last Updated", child = ReactNode("on ${dateFormatter(request.modifiedAt)}")
)