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
	val changeEventHandler: ChangeEventHandler<HTMLInputElement>
)

fun useSearchTerm(): SearchTerm {
	val (searchParams, setSearchParams) = useSearchParams()

	val (searchTerm, debouncedSearchTerm, changeEventHandler) = useDebouncedInput(searchParams.get(SEARCH_PARAM) ?: "")

	useEffect(debouncedSearchTerm, setSearchParams) {
		if (debouncedSearchTerm.isNotBlank() && debouncedSearchTerm != searchParams.get(SEARCH_PARAM)) {
			val params = URLSearchParams()
			params.set("query", debouncedSearchTerm)
			setSearchParams(params)
		}
	}

	return SearchTerm(searchTerm, debouncedSearchTerm, changeEventHandler)
}