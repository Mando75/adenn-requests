package features.dashboard.components


import components.carousel.Carousel
import components.spinners.BarsScaleMiddle
import csstype.ClassName
import features.dashboard.api.useMyRequestsQuery
import lib.render
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.key

external interface IOpenRequestsProps : Props {

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