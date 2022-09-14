package features.search.hooks

import csstype.ClassName
import entities.RequestStatus
import org.w3c.dom.svg.SVGSVGElement
import react.FC
import react.dom.svg.SVGAttributes
import utils.toColor
import wrappers.ArrowDownTrayIcon
import wrappers.CheckCircleIcon
import wrappers.ClockIcon
import wrappers.ExclamationTriangleIcon

data class RequestStatusIcon(val icon: FC<SVGAttributes<SVGSVGElement>>, val className: ClassName)

fun useRequestStatusIcon(status: RequestStatus?): RequestStatusIcon? = status?.let {
	val icon = when (status) {
		RequestStatus.FULFILLED -> CheckCircleIcon
		RequestStatus.REQUESTED -> ClockIcon
		RequestStatus.REJECTED -> ExclamationTriangleIcon
		RequestStatus.WAITING -> ClockIcon
		RequestStatus.IMPORTED -> ClockIcon
		RequestStatus.DOWNLOADING -> ArrowDownTrayIcon
	}
	val baseStyle = "h-6 w-6 text-white p-1 rounded-full"
	val requestStatusColor = status.toColor()
	val className = ClassName("${requestStatusColor.bgColor} $baseStyle")

	return RequestStatusIcon(icon, className)
}