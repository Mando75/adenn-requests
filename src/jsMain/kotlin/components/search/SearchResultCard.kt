package components.search

import context.useIsTouch
import csstype.ClassName
import entities.SearchResult
import kotlinx.js.jso
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import wrappers.useTransition

external interface SearchResultCardProps : Props {
	var searchResult: SearchResult
}

val SearchResultCard = FC<SearchResultCardProps>("SearchResultCard") { props ->
	// HOOKS
	val isTouch = useIsTouch()
	val (transition, toggleDetails) = useTransition(jso {
		preEnter = true
		initialEntered = false
	})

	// STATE
	val showDetails = transition == "entered" || transition == "entering"

	// EFFECTS

	// RENDER
	li {
		className = ClassName("w-full")
		div {
			className =
				ClassName(
					"""overflow-hidden rounded 
					| transform-gpu cursor-default bg-gray-800 bg-cover 
					| outline-none ring-1 transition duration-300 
					| ${
						if (showDetails) "scale-105 shadow-lg ring-gray-500"
						else "scale-100 shadow ring-gray-700"
					}""".trimMargin()
				)
			onMouseEnter = { if (!isTouch) toggleDetails(true) }
			onMouseLeave = { toggleDetails(false) }
			onClick = { toggleDetails(!showDetails) }
			onKeyDown = { e -> if (e.key == "Enter") toggleDetails(true) }
			role = AriaRole.link
			tabIndex = 0

			div {
				className = ClassName("relative inset-0 h-full w-full overflow-hidden")

				img {
					className = ClassName("w-full h-full inset-0 rounded object-cover")
					src = props.searchResult.posterPath
					alt = "${props.searchResult.title}  Poster"
				}
				if (showDetails) {
					SearchResultCardDetail {
						searchResult = props.searchResult
					}
				}
			}
		}
	}
}