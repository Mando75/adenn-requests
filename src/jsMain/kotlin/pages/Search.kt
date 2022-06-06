package pages

import api.queries.useMultiSearchQuery
import components.search.SearchInput
import csstype.ClassName
import hooks.useDebouncedInput
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.ul


val SearchPage = FC<Props>("SearchPage") {

	val (searchTerm, debouncedSearchTerm, changeEventHandler) = useDebouncedInput()
	val searchResultsQuery = useMultiSearchQuery(debouncedSearchTerm)

	section {
		className = ClassName("mt-4")
		SearchInput {
			value = searchTerm
			onChange = changeEventHandler
		}

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
