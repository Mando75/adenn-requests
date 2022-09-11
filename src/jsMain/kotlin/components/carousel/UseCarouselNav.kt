package components.carousel

import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement
import react.RefObject
import react.dom.events.MouseEventHandler
import react.useCallback

enum class Direction(val translateDirection: String) {
	Next("-"),
	Prev("")
}

fun useCarouselNav(
	trackRef: RefObject<HTMLUListElement>,
	container: RefObject<HTMLDivElement>,
	direction: Direction
): MouseEventHandler<HTMLButtonElement> {
	val onClick: MouseEventHandler<HTMLButtonElement> = useCallback(trackRef.current, container.current, direction) {
		val width = when (direction) {
			Direction.Prev -> 0
			Direction.Next -> container.current?.offsetWidth ?: 0
		}

		val style = "translateX(${direction.translateDirection}${width}px)"
		trackRef.current?.style?.transform = style
	}

	return onClick
}
