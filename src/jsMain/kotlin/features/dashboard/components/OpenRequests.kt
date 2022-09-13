package features.dashboard.components


import components.spinners.BarsScaleMiddle
import csstype.ClassName
import features.dashboard.api.useMyRequestsQuery
import kotlinx.js.jso
import lib.render
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.key
import wrappers.Carousel
import wrappers.Responsive
import wrappers.ResponsiveItem

external interface IOpenRequestsProps : Props {

}

private val OpenRequestResponsive = object : Responsive {
	override var desktop: ResponsiveItem = jso {
		breakpoint = jso {
			max = 3000
			min = 1024
		}
		items = 5
	}

	override var tablet: ResponsiveItem = jso {
		breakpoint = jso {
			max = 1024
			min = 464
		}
		items = 3
	}

	override var mobile: ResponsiveItem = jso {
		breakpoint = jso {
			max = 464
			min = 0
		}
		items = 1
	}
}

val OpenRequests = FC<IOpenRequestsProps>("OpenRequests") { props ->
	/// STATE
	val (openRequestsQuery) = useMyRequestsQuery()
	val openRequests = openRequestsQuery.data?.items ?: emptyList()
	val isEmpty = openRequests.isEmpty() && !openRequestsQuery.isLoading

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		h2 {
			className = ClassName("text-4xl")
			+"Your Open Requests"
		}
		openRequestsQuery.isLoading.render {
			div {
				BarsScaleMiddle()
			}
		}
		openRequests.isNotEmpty().render {
			Carousel {
				responsive = OpenRequestResponsive
				partialVisible = true
				deviceType = "mobile"
				showDots = true
				className = ClassName("py-6")
				itemClass = ClassName("px-4")

				openRequests.map { request ->
					DashboardRequestCard {
						key = request.id.toString()
						this.request = request
					}
				}
			}
		}
		isEmpty.render {
			p {
				className = ClassName("text-medium")
				+"You have no open requests"
			}
		}
	}
}