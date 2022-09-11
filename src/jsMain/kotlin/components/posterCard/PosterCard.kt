package components.posterCard


import csstype.ClassName
import kotlinx.js.jso
import lib.reactTransitionState.TransitionState
import providers.useIsTouch
import react.FC
import react.Props
import react.create
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import wrappers.useTransition


external interface PosterCardProps : Props {
	var posterUrl: String
	var posterAlt: String
	var detail: (showDetails: Boolean) -> FC<Props>
	var className: ClassName?
}

val PosterCard = FC<PosterCardProps>("PosterCard") { props ->
	// HOOKS
	val isTouch = useIsTouch()
	val (transition, toggleDetails) = useTransition(jso {
		preEnter = true
		initialEntered = false
	})
	// STATE
	val showDetails = transition == TransitionState.ENTERING || transition == TransitionState.ENTERED

	// EFFECTS

	// RENDER
	li {
		className = props.className
		div {
			className = ClassName(
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

			img {
				className = ClassName("w-full h-full inset-0 rounded object-cover")
				src = props.posterUrl
				alt = props.posterAlt
			}

			+props.detail.invoke(showDetails).create()
		}
	}
}