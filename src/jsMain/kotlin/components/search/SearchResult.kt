package components.search

import csstype.ClassName
import entities.SearchResult
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p

external interface SearchResultProps : Props {
	var searchResult: SearchResult
}

val SearchResult = FC<SearchResultProps> { props ->
	li {
		div {
			className = ClassName("w-full")

			p {
				+props.searchResult.title
			}
		}
	}
}