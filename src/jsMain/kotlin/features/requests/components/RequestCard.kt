package features.requests.components


import csstype.ClassName
import entities.RequestEntity
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p

external interface RequestCardProps : Props {
	var request: RequestEntity
}

val RequestCard = FC<RequestCardProps>("RequestCard") { props ->
	// HOOKS
	// STATE


	// EFFECTS

	// RENDER
	li {
		className = ClassName("w-full flex")
		div {
			className = ClassName(
				"""
				| rounded
				| cursor-default bg-blue-900 bg-cover
				| outline-none ring-1 shadow ring-blue-700
			""".trimMargin()
			)

			div {
				className = ClassName(
					"""
					| relative m-4 rounded flex w-full gap-4 p-2
				""".trimMargin()
				)
				img {
					className =
						ClassName(
							"""z-10 flex-shrink-0 scale-100  max-w-md
								| overflow-hidden rounded-md transition transform-gpu
								| duration-300 hover:scale-105
								""".trimMargin()
						)
					src = props.request.posterPath
					alt = "Poster for ${props.request.title}"
				}
				div {
					className = ClassName(
						"""flex flex-col flex-grow-1
						| text-white
					""".trimMargin()
					)
					h4 {
						className = ClassName("text-3xl font-semibold")
						+props.request.title
					}
					p {
						className = ClassName("text-base")
						+props.request.overview
					}
				}
			}
		}
	}
}