package components.search


import csstype.ClassName
import entities.SearchResult
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface SearchResultCardDetailProps : Props {
	var searchResult: SearchResult
}

val SearchResultCardDetail = FC<SearchResultCardDetailProps>("SearchResultCardDetail") { props ->
	// STATE
	val year = props.searchResult.releaseDate?.substring(0, 4) ?: "Unknown"
	// HOOKS

	// EFFECTS

	// RENDER

	div {
		span {
			className = ClassName("text-white text-sm font-bold")
			+year
		}
		h3 {
			className = ClassName("text-white text-2xl font-bold")

			+props.searchResult.title
		}
		p {
			className = ClassName("text-white line-clamp-3")

			+props.searchResult.overview
		}
	}
}