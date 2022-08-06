package features.search.components

import components.posterCard.PosterCard
import components.posterCard.PosterCardDetail
import entities.SearchResultEntity
import features.search.providers.RequestModalContext
import react.FC
import react.Props
import react.useContext

external interface SearchResultCardProps : Props {
	var searchResult: SearchResultEntity
}

val SearchResultCard = FC<SearchResultCardProps>("SearchResultCard") { props ->
	// HOOKS
	val modalState = useContext(RequestModalContext)

	// STATE

	// EFFECTS

	// RENDER
	PosterCard {
		posterUrl = props.searchResult.posterPath.value
		posterAlt = "Poster for ${props.searchResult.title}"
		detail = { showDetails ->
			FC("PosterDetailWrapper") {
				PosterCardDetail {
					title = props.searchResult.title
					overview = props.searchResult.overview
					isMovie = props.searchResult is SearchResultEntity.MovieResult
					releaseDate = props.searchResult.releaseDate
					requestStatus = props.searchResult.request?.status
					showDetail = showDetails

					props.searchResult.request?.let { request ->
						RequestInfoLink {
							status = request.status
						}
					} ?: RequestButton {
						onClick = { modalState.setOpen(true) }
					}
				}
			}
		}
	}
}