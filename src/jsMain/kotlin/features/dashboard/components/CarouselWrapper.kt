package features.dashboard.components


import api.RequestsQueryResponse
import components.spinners.BarsScaleMiddle
import csstype.ClassName
import hooks.UsePagination
import lib.carousel.CarouselBreakpoints
import lib.render
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.key
import react.query.UseQueryResult
import wrappers.Carousel

external interface ICarouselWrapperProps : Props {
	var queryResult: Pair<UseQueryResult<RequestsQueryResponse, Error>, UsePagination>
	var title: String
	var emptyMessage: String
}

val CarouselWrapper = FC<ICarouselWrapperProps>("CarouselWrapper") { props ->
	/// STATE
	val (query) = props.queryResult
	val data = query.data?.items ?: emptyList()
	val isEmpty = data.isEmpty() && !query.isLoading

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		h2 {
			className = ClassName("text-4xl")
			+props.title
		}
		query.isLoading.render {
			div {
				BarsScaleMiddle()
			}
		}
		data.isNotEmpty().render {
			Carousel {
				responsive = CarouselBreakpoints
				partialVisible = true
				deviceType = "mobile"
				showDots = true
				className = ClassName("py-6")
				itemClass = ClassName("px-4")

				data.map { item ->
					DashboardRequestCard {
						key = item.id.toString()
						request = item
					}
				}
			}
		}
		isEmpty.render {
			p {
				className = ClassName("text-medium")
				+props.emptyMessage
			}
		}
	}
}