package features.requests.components


import csstype.ClassName
import entities.RequestEntity
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface RequestCardOverviewProps : Props {
	var request: RequestEntity
	var className: ClassName?
}

val RequestCardOverview = FC<RequestCardOverviewProps>("RequestCardOverview") { props ->
	// STATE
	val year = props.request.media.releaseDate?.substring(0, 4) ?: "Unknown"
	val overview = props.request.media.overview ?: ""
	// HOOKS

	// EFFECTS

	// RENDER
	div {
		className = ClassName("flex flex-col justify-center text-white ${props.className ?: ""}")
		span {
			className = ClassName("text-sm md:text-lg font-semibold")

			+year
		}
		h4 {
			className = ClassName("text-xl md:text-3xl font-bold")
			+props.request.title
		}
		p {
			className = ClassName("text-base hidden md:line-clamp-5")
			title = overview

			+overview
		}
	}
}