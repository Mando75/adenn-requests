package features.requests.routes


import csstype.ClassName
import features.requests.api.useRequestsQuery
import features.requests.hooks.useRequestFilters
import features.search.components.SearchInput
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section

val RequestsPage = FC<Props>("RequestsPage") {
	// STATE
	val (requestFilters, searchTerm, changeEventHandler) = useRequestFilters()
	val (requestsQuery, pagination) = useRequestsQuery(requestFilters)

	// HOOKS

	// EFFECTS

	// RENDER
	section {
		className = ClassName("mt-4")

		SearchInput {
			placeholder = "Search Requests"
			value = searchTerm
			onChange = changeEventHandler
		}

		requestsQuery.data?.let { data ->
			div {
				p { +data.totalPages.toString() }
				p { +data.items.toString() }
			}
		}
	}
}