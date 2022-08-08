package features.search.routes

import csstype.ClassName
import features.search.api.useMultiSearchQuery
import features.search.components.RequestModal
import features.search.components.SearchInput
import features.search.components.SearchResultList
import features.search.hooks.useSearchTerm
import features.search.providers.RequestModalProvider
import react.FC
import react.Props
import react.dom.html.ReactHTML.section


val SearchPage = FC<Props>("SearchPage") {
	// STATE
	val (searchTerm, debouncedSearchTerm, changeEventHandler, clearEventHandler) = useSearchTerm()
	val searchResultsQuery = useMultiSearchQuery(debouncedSearchTerm)


	// RENDER
	RequestModalProvider {
		section {
			className = ClassName("mt-4")
			SearchInput {
				value = searchTerm
				onChange = changeEventHandler
				onClear = { clearEventHandler() }
			}

			SearchResultList {
				isIdle = searchResultsQuery.isIdle
				isLoading = searchResultsQuery.isLoading
				items = searchResultsQuery.data ?: emptyList()
			}
		}
		RequestModal()
	}
}
