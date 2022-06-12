package components.search


import csstype.ClassName
import entities.SearchResult
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface SearchResultCardDetailProps : Props {
	var searchResult: SearchResult
}

val SearchResultCardDetail = FC<SearchResultCardDetailProps>("SearchResultCardDetail") { props ->
	// STATE
	val year = props.searchResult.releaseDate?.substring(0, 4) ?: "Unknown"
	// HOOKS

	// EFFECTS

	// RENDER

	ReactHTML.div {
		className =
			ClassName("absolute inset-0 left-0 right-0 flex flex-col justify-between p-2 bg-slate-600 bg-opacity-80")

		ReactHTML.span {
			className = ClassName("text-white text-sm font-bold")
			+year
		}
		ReactHTML.h3 {
			className = ClassName("text-white text-2xl font-bold")

			+props.searchResult.title
		}
		ReactHTML.p {
			className = ClassName("text-white")

			+props.searchResult.overview
		}
	}
}