package components.search


import csstype.ClassName
import entities.SearchResult
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

external interface SearchResultCardDetailProps : PropsWithChildren {
	var searchResult: SearchResult
	var showDetail: Boolean
}

val SearchResultCardDetail = FC<SearchResultCardDetailProps>("SearchResultCardDetail") { props ->
	// STATE
	val year = props.searchResult.releaseDate?.substring(0, 4) ?: "Unknown"
	val isMovie = props.searchResult is SearchResult.MovieResult
	val mediaType = if (isMovie) "Movie" else "TV Show"
	// HOOKS

	// EFFECTS

	// RENDER
	div {
		className =
			ClassName(
				"""absolute inset-0 left-0 right-0 flex flex-col justify-between p-2
							| transition duration-100 ${if (props.showDetail) "bg-slate-600 bg-opacity-80" else ""}
						""".trimMargin()
			)
		// Top row with media type and request status indicator
		div {
			className = ClassName("flex flex-row justify-between")

			span {
				className = ClassName(
					"""text-white text-sm font-medium rounded-full px-2 py-1
								| ${if (isMovie) "bg-purple-500" else "bg-blue-500"}
							""".trimMargin()
				)

				+mediaType
			}
		}
		// Details title, year, and description
		if (props.showDetail) {
			div {
				className = ClassName("flex flex-col")


				div {
					span {
						className = ClassName("text-white text-sm font-bold")
						+year
					}
					ReactHTML.h3 {
						className = ClassName("text-white text-2xl font-bold")

						+props.searchResult.title
					}
					ReactHTML.p {
						className = ClassName("text-white line-clamp-3")

						+props.searchResult.overview
					}
				}
				// For Request Button
				+props.children
			}
		}
	}
}