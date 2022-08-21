package utils

import csstype.ClassName
import entities.RequestStatus

class RequestStatusColor(
	val bgColor: String,
	val hoverBgColor: String,
	val focusBgColor: String,
	val focusRingColor: String
) {
	fun bgClassName() = ClassName(bgColor)
	fun allClassName() = ClassName("$bgColor $hoverBgColor $focusBgColor $focusRingColor")
}

fun RequestStatus.toColor(): RequestStatusColor = when (this) {
	RequestStatus.REQUESTED -> RequestStatusColor(
		"bg-blue-500",
		"hover:bg-blue-600",
		"focus:bg-blue-700",
		"focus:ring-blue-800"
	)

	RequestStatus.FULFILLED -> RequestStatusColor(
		"bg-green-500",
		"hover:bg-green-600",
		"focus:bg-green-700",
		"focus:ring-green-800"
	)

	RequestStatus.REJECTED -> RequestStatusColor(
		"bg-red-500",
		"hover:bg-red-600",
		"focus:bg-red-700",
		"focus:ring-red-800"
	)

	RequestStatus.WAITING -> RequestStatusColor(
		"bg-purple-500",
		"hover:bg-purple-600",
		"focus:bg-purple-700",
		"focus:ring-purple-800"
	)

	RequestStatus.IMPORTED -> RequestStatusColor(
		"bg-blue-500",
		"hover:bg-blue-600",
		"focus:bg-blue-700",
		"focus:ring-blue-800"
	)

	RequestStatus.DOWNLOADING -> RequestStatusColor(
		"bg-cyan-500",
		"hover:bg-cyan-600",
		"focus:bg-cyan-700",
		"focus:ring-cyan-800"
	)
}