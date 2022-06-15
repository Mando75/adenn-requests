package hooks

import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlinx.js.jso
import kotlinx.js.timers.setTimeout
import org.w3c.dom.events.Event
import react.dom.events.PointerEvent
import react.dom.events.PointerType
import react.useEffectOnce
import react.useState
import kotlin.time.Duration.Companion.milliseconds

private val UPDATE_INTERVAL = 1000.milliseconds

fun useInteraction(): Boolean {
	val (isTouch, setIsTouch) = useState(false)

	useEffectOnce {
		val hasTapEvent = js("'ontouchstart' in window") as Boolean
		setIsTouch(hasTapEvent)

		var localTouch = hasTapEvent
		var lastTouchUpdate = Clock.System.now()

		val shouldUpdate = { lastTouchUpdate + UPDATE_INTERVAL < Clock.System.now() }

		val onMouseMove = { _: Event? ->
			if (localTouch && shouldUpdate()) {
				setTimeout(UPDATE_INTERVAL) {
					if (shouldUpdate()) {
						setIsTouch(false)
						localTouch = false
					}
				}
			}
		}

		val onTouchStart = { _: Event? ->
			lastTouchUpdate = Clock.System.now()
			if (!localTouch) {
				setIsTouch(true)
				localTouch = true
			}
		}

		val onPointerMove = { e: Event ->
			when ((e as PointerEvent<*>).pointerType) {
				PointerType.pen, PointerType.touch -> onTouchStart(null)
				else -> onMouseMove(null)
			}
		}

		if (hasTapEvent) {
			window.addEventListener("mousemove", onMouseMove, jso {})
			window.addEventListener("touchstart", onTouchStart, jso {})
		} else {
			window.addEventListener("pointerdown", onPointerMove, jso {})
			window.addEventListener("pointermove", onPointerMove, jso {})
		}

		cleanup {
			if (hasTapEvent) {
				window.removeEventListener("mousemove", onMouseMove, jso {})
				window.removeEventListener("touchstart", onTouchStart, jso {})
			} else {
				window.removeEventListener("pointerdown", onPointerMove, jso {})
				window.removeEventListener("pointermove", onPointerMove, jso {})
			}
		}
	}

	return isTouch
}
