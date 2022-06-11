package components.common.listView


import components.search.SearchResultCard
import csstype.ClassName
import entities.SearchResult
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul

external interface ListViewProps : Props {
	var items: List<SearchResult>
	var isEmpty: Boolean
	var isLoading: Boolean
}

val ListView = FC<ListViewProps>("ListView") { props ->
	// COMPONENT BODY
	if (props.isEmpty) {
		div {
			className = ClassName("mt-64 w-full text-center text-2xl text-gray-400 rounded-sm")
			+"No results found"
		}
	} else {
		ul {
			className = ClassName("grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-4 mr-4")
			props.items.map { result -> SearchResultCard { searchResult = result} }
		}
	}
}