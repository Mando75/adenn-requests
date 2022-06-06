package pages

import api.queries.useMultiSearchQuery
import components.search.SearchInput
import csstype.ClassName
import hooks.useInput
import middleware.RequireAuth
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.ul


private val Search = FC<Props>("SearchPage") {

	val (searchTerm, onSearchChange) = useInput()
	val searchResultsQuery = useMultiSearchQuery(searchTerm)

	section {
		className = ClassName("mt-4")
		SearchInput {
			value = searchTerm
			onChange = onSearchChange
		}
		p {
			+searchTerm
		}
//
		if (searchResultsQuery.isIdle) div { +"Start typing to search" }
		else if (searchResultsQuery.isLoading) div { +"Loading..." }
		else if (searchResultsQuery.isError) div { +(searchResultsQuery.error?.message ?: "Unknown error") }
		else if (searchResultsQuery.data.isNullOrEmpty()) div { +"No results" }
		else {
			div {
				+"Results: "
				ul {
					searchResultsQuery.data?.map { result ->
						li {
							+result.title
						}
					}
				}
			}
		}
	}
}


val SearchPage = RequireAuth.create {
	Search()
}