package features.search.components


import components.spinners.BarsScaleMiddle
import csstype.ClassName
import entities.SearchResultEntity
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul

external interface SearchResultListProps : Props {
	var items: List<SearchResultEntity>
	var isLoading: Boolean
	var isIdle: Boolean
}

val SearchResultList = memo(FC<SearchResultListProps>("SearchResultList") { props ->
	// STATE
	val isEmpty = !props.isLoading && props.items.isEmpty() && !props.isIdle
	val message: ReactNode = if (isEmpty) {
		ReactNode("No results found")
	} else if (props.isLoading) {
		BarsScaleMiddle.create {
			className = ClassName("fill-gray-400")
			width = 150.0
			height = 150.0
		}
	} else if (props.isIdle) {
		ReactNode("Start typing to search")
	} else {
		ReactNode("")
	}

	// COMPONENT BODY

	if (isEmpty || props.isLoading || props.isIdle) {
		div {
			className = ClassName("mt-64 w-full flex justify-center text-center text-2xl text-gray-400 rounded-sm")

			+message
		}
	} else {
		ul {
			className = ClassName("grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-8")
			props.items.map { result -> SearchResultCard { searchResult = result } }
		}
	}
})