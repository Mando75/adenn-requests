package features.search.components


import csstype.ClassName
import entities.SearchResultEntity
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul

external interface SearchResultListProps : Props {
	var items: List<SearchResultEntity>
	var isLoading: Boolean
}

val SearchResultList = FC<SearchResultListProps>("SearchResultList") { props ->
	// STATE
	val isEmpty = !props.isLoading && props.items.isEmpty()

	// COMPONENT BODY

	if (isEmpty || props.isLoading) {
		div {
			className = ClassName("mt-64 w-full text-center text-2xl text-gray-400 rounded-sm")
			+(if (isEmpty) "No results found" else "Loading...")
		}
	} else {
		ul {
			className = ClassName("grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-8")
			props.items.map { result -> SearchResultCard { searchResult = result } }
		}
	}
}