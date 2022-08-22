package features.requests.components


import csstype.BackgroundImage
import csstype.ClassName
import emotion.react.css
import entities.RequestEntity
import features.requests.hooks.useRequestDetailLineItems
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.dl
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li

external interface RequestCardProps : Props {
	var request: RequestEntity
}

val RequestCard = FC<RequestCardProps>("RequestCard") { props ->
	// HOOKS
	val requestDetailLineItems = useRequestDetailLineItems(props.request)
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
				className = ClassName("relative grid grid-cols-7 gap-4 p-4 w-full bg-gray-700/50")
				img {
					className =
						ClassName(
							""" 
								| hidden md:block md:col-span-1 z-10 shrink scale-100 rounded-md
								| overflow-hidden transition transform-gpu
								| duration-300 hover:scale-105
								""".trimMargin()
						)
					src = props.request.media.posterPath.value
					alt = "Poster for ${props.request.title}"
				}
				RequestCardOverview {
					request = props.request
					className = ClassName("md:col-span-2 row-span-2")
				}
				dl {
					className = ClassName("col-span-2 md:col-span-2 text-white gap-4 flex flex-col justify-center")
					requestDetailLineItems.map { detail ->
						RequestDetailLineItem {
							label = detail.label

							+detail.child
						}
					}
				}
				div {
					className = ClassName("col-span-2 md:col-span-2 flex w-full items-center")
					RequestActions()
				}
			}
		}
	}
}