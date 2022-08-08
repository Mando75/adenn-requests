package features.search.hooks

import hooks.useDebouncedInput
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URLSearchParams
import react.dom.events.ChangeEventHandler
import react.router.dom.useSearchParams
import react.useEffect

private const val SEARCH_PARAM = "query"

data class SearchTerm(
	val searchTerm: String,
	val debouncedSearchTerm: String,
	val changeEventHandler: ChangeEventHandler<HTMLInputElement>,
	val clearHandler: () -> Unit
)

fun useSearchTerm(): SearchTerm {
	val (searchParams, setSearchParams) = useSearchParams()

	val (searchTerm, debouncedSearchTerm, changeEventHandler, clearHandler) = useDebouncedInput(
		searchParams.get(
			SEARCH_PARAM
		) ?: ""
	)

	useEffect(debouncedSearchTerm, setSearchParams) {
		val params = URLSearchParams()
		if (debouncedSearchTerm.isNotBlank() && debouncedSearchTerm != searchParams.get(SEARCH_PARAM)) {
			params.set("query", debouncedSearchTerm)
		}
		setSearchParams(params)
	}

	return SearchTerm(searchTerm, debouncedSearchTerm, changeEventHandler, clearHandler)
}