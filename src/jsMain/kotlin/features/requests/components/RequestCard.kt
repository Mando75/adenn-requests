package features.requests.components


import components.posterCard.PosterCard
import components.posterCard.PosterCardDetail
import entities.RequestEntity
import react.FC
import react.Props

external interface RequestCardProps : Props {
	var request: RequestEntity
}

val RequestCard = FC<RequestCardProps>("RequestCard") { props ->
	// HOOKS
	// STATE


	// EFFECTS

	// RENDER
	PosterCard {
		posterUrl = props.request.posterPath
		posterAlt = "Poster for ${props.request.title}"
		detail = { showDetails ->
			FC("PosterDetailWrapper") {
				PosterCardDetail {
					showDetail = showDetails
					title = props.request.title
					isMovie = props.request is RequestEntity.MovieRequest
					releaseDate = props.request.releaseDate
					request = props.request
					overview = props.request.overview
				}
			}
		}
	}
}