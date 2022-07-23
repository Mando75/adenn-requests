package components.posterCard


import components.tag.BlueTag
import components.tag.PurpleTag
import components.tag.Tag
import csstype.ClassName
import entities.RequestStatus
import features.search.hooks.useRequestStatusIcon
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface PosterCardDetailProps : PropsWithChildren {
	var title: String
	var overview: String
	var isMovie: Boolean
	var releaseDate: String?
	var showDetail: Boolean
	var requestStatus: RequestStatus?
}

val PosterCardDetail = FC<PosterCardDetailProps>("PosterCardDetail") { props ->
	// STATE
	val year = props.releaseDate?.substring(0, 4) ?: "Unknown"
	val mediaType = if (props.isMovie) "Movie" else "TV Show"

	// HOOKS
	val requestStatusIcon = useRequestStatusIcon(props.requestStatus)

	// EFFECTS

	// RENDER
	div {
		className = ClassName(
			"""absolute inset-0 left-0 right-0 flex flex-col justify-between p-2
							| transition duration-100 ${if (props.showDetail) "bg-slate-600 bg-opacity-80" else ""}
						""".trimMargin()
		)
		// Top row with media type and request status indicator
		div {
			className = ClassName("flex flex-row justify-between")

			Tag {
				style = if (props.isMovie) PurpleTag() else BlueTag()

				+mediaType
			}

			requestStatusIcon?.let { requestIcon ->
				div {

					requestIcon.icon {
						className = requestIcon.className
					}
				}
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
					h3 {
						className = ClassName("text-white text-2xl font-bold")
						title = props.title

						+props.title
					}
					p {
						className = ClassName("text-white line-clamp-3")
						title = props.overview

						+props.overview
					}
				}
				// For Action Button
				+props.children
			}
		}
	}
}