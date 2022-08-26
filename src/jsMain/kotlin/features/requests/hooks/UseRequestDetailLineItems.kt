package features.requests.hooks

import components.tag.Tag
import csstype.ClassName
import entities.RequestEntity
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.span
import utils.dateFormatter
import utils.toColor

data class RequestDetailLineItem(
	val label: String, val child: ReactNode
)

fun useRequestDetailLineItems(request: RequestEntity): List<RequestDetailLineItem> {
	val status = createStatus(request)
	val requested = requested(request)
	val updated = updated(request)

	return listOf(status, requested, updated)
}

private fun createStatus(request: RequestEntity): RequestDetailLineItem {
	return RequestDetailLineItem(label = "Status ", child = Tag.create {
		className = request.status.toColor().bgClassName()
		+request.status.name
	})
}

private fun requested(request: RequestEntity): RequestDetailLineItem {
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

private fun updated(request: RequestEntity) = RequestDetailLineItem(
	label = "Last Updated", child = ReactNode("on ${dateFormatter(request.modifiedAt)}")
)