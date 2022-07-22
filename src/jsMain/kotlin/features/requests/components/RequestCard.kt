package features.requests.components


import csstype.BackgroundImage
import csstype.ClassName
import emotion.react.css
import entities.RequestListItem
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p

external interface RequestCardProps : Props {
	var request: RequestListItem
}

val RequestCard = FC<RequestCardProps>("RequestCard") { props ->
	// HOOKS
	// STATE
	val backdropPath = props.request.media.backdropPath?.value?.let { bg -> "url(${bg})".unsafeCast<BackgroundImage>() }

	// EFFECTS

	// RENDER
	li {
		className = ClassName("w-full flex")

		div {
			val classes =
				ClassName(
					"""w-full rounded bg-cover bg-no-repeat bg-center cursor-default outline-none shadow
					| ${if (backdropPath == null) "bg-gradient-to-r from-cyan-300 to-blue-700" else ""}""".trimMargin()
				)

			className = classes
			backdropPath?.let {
				css(classes) {
					backgroundImage = backdropPath
				}
			}

			div {
				className = ClassName(
					"""
					| relative flex gap-4 p-4 w-full bg-gray-600/25
				""".trimMargin()
				)
				img {
					className =
						ClassName(
							""" 
								| z-10 shrink scale-100 rounded-md
								| overflow-hidden transition transform-gpu
								| duration-300 hover:scale-105
								""".trimMargin()
						)
					src = props.request.media.posterPath.value
					alt = "Poster for ${props.request.title}"
				}
				div {
					className = ClassName(
						"""flex flex-col grow
						| text-white
					""".trimMargin()
					)
					h4 {
						className = ClassName("text-3xl font-semibold")
						+props.request.title
					}
					p {
						className = ClassName("text-base")
						+(props.request.media.overview ?: "")
					}
				}
			}
		}
	}
}