package features.dashboard.components


import components.posterCard.PosterCard
import components.posterCard.PosterCardDetail
import csstype.ClassName
import entities.RequestEntity
import features.search.components.RequestInfoLink
import react.FC
import react.Props

external interface IDashboardRequestCardProps : Props {
	var request: RequestEntity
}

val DashboardRequestCard = FC<IDashboardRequestCardProps>("DashboardRequestCard") { props ->
	/// STATE
	val media = props.request.media

	/// HOOKS

	/// EFFECTS

	/// RENDER
	PosterCard {
		posterUrl = media.posterPath.value
		posterAlt = "Poster for ${media.title}"
		className = ClassName("basis-64 shrink-0")
		detail = { showDetails ->
			FC("DashboardDetailWrapper") {
				PosterCardDetail {
					title = media.title
					overview = media.overview ?: "Unavailable"
					isMovie = props.request is RequestEntity.MovieRequest
					releaseDate = media.releaseDate
					requestStatus = props.request.status
					showDetail = showDetails

					RequestInfoLink {
						status = props.request.status
					}
				}
			}
		}
	}
}