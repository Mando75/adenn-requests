package features.search.routes

import components.listView.ListView
import csstype.ClassName
import features.search.api.useMultiSearchQuery
import features.search.components.SearchInput
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
				items = searchResultsQuery.data ?: emptyList()
			}
		}
	}
}