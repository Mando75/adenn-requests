package features.requests.routes


import csstype.ClassName
import features.requests.api.useRequestsQuery
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section

val RequestsPage = FC<Props>("RequestsPage") {
	// STATE
	val requestsQuery = useRequestsQuery()

	// HOOKS

	// EFFECTS

	// RENDER
	section {
		className = ClassName("mt-4")

		requestsQuery.data?.let { data ->
			div {
				p { +data.totalCount.toString() }
				p { +data.items.toString() }
			}
		}
	}
}