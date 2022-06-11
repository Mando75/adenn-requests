package components.search

import context.useIsTouch
import csstype.ClassName
import entities.SearchResult
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState

external interface SearchResultCardProps : Props {
	var searchResult: SearchResult
}

val SearchResultCard = FC<SearchResultCardProps> { props ->
	// HOOKS
	val isTouch = useIsTouch()
	// STATE
	val (showDetail, setShowDetail) = useState(false)
	val year = props.searchResult.releaseDate?.substring(0, 4) ?: "Unknown"

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
						if (showDetail) "scale-105 shadow-lg ring-gray-500"
						else "scale-100 shadow ring-gray-700"
					}""".trimMargin()
				)
			onMouseEnter = { if (!isTouch) setShowDetail(true) }
			onMouseLeave = { setShowDetail(false) }
			onClick = { setShowDetail(true) }
			onKeyDown = { e -> if (e.key == "Enter") setShowDetail(true) }
			role = AriaRole.link
			tabIndex = 0

			div {
				className = ClassName("relative inset-0 h-full w-full overflow-hidden")

				img {
					className = ClassName("w-full h-full inset-0 rounded object-cover")
					src = props.searchResult.posterPath
					alt = "${props.searchResult.title}  Poster"
				}
				if (showDetail) {
					div {
						className =
							ClassName("absolute inset-0 left-0 right-0 flex flex-col justify-between p-2 hover:bg-slate-600 hover:bg-opacity-80")

						span {
							className = ClassName("text-white text-sm font-bold")
							+year
						}
						h3 {
							className = ClassName("text-white text-2xl font-bold")

							+props.searchResult.title
						}
						p {
							className = ClassName("text-white")

							+props.searchResult.overview
						}
					}
				}
			}
		}

	}
}