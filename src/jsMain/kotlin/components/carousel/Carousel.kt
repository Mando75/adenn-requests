package components.carousel


import csstype.ClassName
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul
import react.useRef
import wrappers.ChevronLeftIcon
import wrappers.ChevronRightIcon

external interface ICarouselProps : PropsWithChildren {

}

val Carousel = FC<ICarouselProps>("Carousel") { props ->
	/// STATE
	val containerRef = useRef<HTMLDivElement>()
	val trackRef = useRef<HTMLUListElement>()

	/// HOOKS
	val handleNext = useCarouselNav(trackRef, containerRef, Direction.Next)
	val handlePrev = useCarouselNav(trackRef, containerRef, Direction.Prev)

	/// EFFECTS

	/// RENDER
	/// Carousel Container
	div {
		ref = containerRef
		className = ClassName("m-4 min-h-[200px] relative")
		/// Carousel Inner
		div {
			className = ClassName("overflow-hidden")
			/// Track
			ul {
				ref = trackRef
				className = ClassName("flex flex-row gap-4 transition duration-300 p-4")
				/// Cards
				+props.children
			}

		}
		/// Navigation
		div {
			/// Previous
			CarouselNavIcon {
				onClick = handlePrev
				className = ClassName("-left-8")
				ChevronLeftIcon {
					className = ClassName("w-8 h-8")
				}
			}

			/// Next
			CarouselNavIcon {
				onClick = handleNext
				className = ClassName("-right-8")
				ChevronRightIcon {
					className = ClassName("w-8 h-8")
				}
			}
		}
	}
}
