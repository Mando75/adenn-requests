package features.search.components

import components.posterCard.PosterCard
import components.posterCard.PosterCardDetail
import entities.SearchResult
import features.search.api.useSubmitRequestMutation
import lib.reactQuery.exec
import react.FC
import react.Props

external interface SearchResultCardProps : Props {
	var searchResult: SearchResult
}

val SearchResultCard = FC<SearchResultCardProps>("SearchResultCard") { props ->
	// HOOKS
	val submitRequestMutation = useSubmitRequestMutation()

	// STATE

	// EFFECTS

	// RENDER
	PosterCard {
		posterUrl = props.searchResult.posterPath
		posterAlt = "Poster for ${props.searchResult.title}"
		detail = { showDetails ->
			FC("PosterDetailWrapper") {
				PosterCardDetail {
					title = props.searchResult.title
					overview = props.searchResult.overview
					isMovie = props.searchResult is SearchResult.MovieResult
					releaseDate = props.searchResult.releaseDate
					request = props.searchResult.request
					showDetail = showDetails

					props.searchResult.request?.let { request ->
						RequestInfoLink {
							status = request.status
						}
					} ?: RequestButton {
						onClick = { submitRequestMutation.exec(props.searchResult) }
					}
				}
			}
		}
	}
}