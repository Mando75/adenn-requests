package pages

import api.queries.useMultiSearchQuery
import components.common.listView.ListView
import components.search.SearchInput
import csstype.ClassName
import hooks.useDebouncedInput
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.section


val SearchPage = FC<Props>("SearchPage") {
	// STATE
	val (searchTerm, debouncedSearchTerm, changeEventHandler) = useDebouncedInput()
	val searchResultsQuery = useMultiSearchQuery(debouncedSearchTerm)

	// RENDER
	section {
		className = ClassName("mt-4")
		SearchInput {
			value = searchTerm
			onChange = changeEventHandler
		}

		if (searchResultsQuery.isIdle) div { +"Start typing to search" }
		else {
			ListView {
				isLoading = searchResultsQuery.isLoading
				isEmpty = searchResultsQuery.data.isNullOrEmpty()
				items = searchResultsQuery.data ?: emptyList()
			}
		}
	}
}
