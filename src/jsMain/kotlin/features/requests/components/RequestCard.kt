package features.requests.components


import csstype.BackgroundImage
import csstype.ClassName
import emotion.react.css
import entities.RequestListItem
import features.requests.hooks.useRequestDetailLineItems
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.dl
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface RequestCardProps : Props {
	var request: RequestListItem
}

val RequestCard = FC<RequestCardProps>("RequestCard") { props ->
	// HOOKS
	val requestDetailLineItems = useRequestDetailLineItems(props.request)
	// STATE
	val backdropPath = props.request.media.backdropPath?.value?.let { bg -> "url(${bg})".unsafeCast<BackgroundImage>() }
	val year = props.request.media.releaseDate?.substring(0, 4) ?: "Unknown"
	val overview = props.request.media.overview ?: ""

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
					| relative grid grid-cols-6 gap-4 p-4 w-full bg-gray-600/50
				""".trimMargin()
				)
				img {
					className =
						ClassName(
							""" 
								| col-span-1 z-10 shrink scale-100 rounded-md
								| overflow-hidden transition transform-gpu
								| duration-300 hover:scale-105
								""".trimMargin()
						)
					src = props.request.media.posterPath.value
					alt = "Poster for ${props.request.title}"
				}
				div {
					className = ClassName(
						"""col-span-2 flex flex-col grow justify-center
						| text-white
					""".trimMargin()
					)
					span {
						className = ClassName("text-large font-semibold")

						+year
					}
					h4 {
						className = ClassName("text-3xl font-bold")
						+props.request.title
					}
					p {
						className = ClassName("text-base")
						+overview
					}
				}
				dl {
					className = ClassName(
						"""col-span-3 text-white gap-4 flex flex-col justify-center"""
					)
					requestDetailLineItems.map { detail ->
						RequestDetailLineItem {
							label = detail.label

							+detail.child
						}
					}
				}
			}
		}
	}
}