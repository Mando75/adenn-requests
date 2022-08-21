package features.requests.hooks

import entities.RequestStatus
import entities.UserType
import providers.useSessionUser
import utils.toColor

class RequestAction(val label: String, val value: RequestStatus) {
	fun color() = value.toColor()
}

private val REQUESTED = RequestAction("Set as Requested", RequestStatus.REQUESTED)
private val FULFILLED = RequestAction("Set as Fulfilled", RequestStatus.FULFILLED)
private val REJECTED = RequestAction("Set as Rejected", RequestStatus.REJECTED)
private val WAITING = RequestAction("Set as Waiting", RequestStatus.WAITING)
private val IMPORTED = RequestAction("Set as Imported", RequestStatus.IMPORTED)
private val DOWNLOADING = RequestAction("Set as Downloading", RequestStatus.DOWNLOADING)

private val ADMIN_ACTIONS = listOf(REQUESTED, FULFILLED, REJECTED, WAITING, IMPORTED, DOWNLOADING)
private val DEFAULT_ACTIONS = emptyList<RequestAction>()

fun useAllowedActions(): List<RequestAction> {
	val user = useSessionUser()

	return when (user?.userType) {
		UserType.ADMIN -> ADMIN_ACTIONS
		UserType.DEFAULT -> DEFAULT_ACTIONS
		null -> emptyList()
	}
}