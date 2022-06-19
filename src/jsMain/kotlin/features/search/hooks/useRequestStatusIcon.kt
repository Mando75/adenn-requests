package features.search.hooks

import csstype.ClassName
import entities.RequestEntity
import entities.RequestStatus
import org.w3c.dom.svg.SVGSVGElement
import react.FC
import react.dom.svg.SVGAttributes
import wrappers.CheckCircleIcon
import wrappers.ClockIcon
import wrappers.CloudDownloadIcon
import wrappers.ExclamationIcon

data class RequestStatusIcon(val icon: FC<SVGAttributes<SVGSVGElement>>, val className: ClassName)

fun useRequestStatusIcon(request: RequestEntity?): RequestStatusIcon? = request?.let {
	val icon = when (it.status) {
		RequestStatus.FULFILLED -> CheckCircleIcon
		RequestStatus.REQUESTED -> ClockIcon
		RequestStatus.REJECTED -> ExclamationIcon
		RequestStatus.WAITING -> ClockIcon
		RequestStatus.IMPORTED -> ClockIcon
		RequestStatus.DOWNLOADING -> CloudDownloadIcon
	}
	val baseStyle = "h-6 w-6 text-white p-1 rounded-full"
	val className = when (it.status) {
		RequestStatus.REQUESTED -> ClassName("bg-blue-500 $baseStyle")
		RequestStatus.FULFILLED -> ClassName("bg-green-500 $baseStyle")
		RequestStatus.REJECTED -> ClassName("bg-red-500 $baseStyle")
		RequestStatus.WAITING -> ClassName("bg-purple-500 $baseStyle")
		RequestStatus.IMPORTED -> ClassName("bg-blue-500 $baseStyle")
		RequestStatus.DOWNLOADING -> ClassName("bg-cyan-500 $baseStyle")
	}

	return RequestStatusIcon(icon, className)
}