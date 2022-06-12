package hooks

import kotlinx.js.timers.clearTimeout
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLInputElement
import react.dom.events.ChangeEventHandler
import react.useEffect
import react.useState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun useInput(defaultValue: String = ""): Pair<String, ChangeEventHandler<HTMLInputElement>> {
	val (input, setInput) = useState(defaultValue)
	val changeHandler: ChangeEventHandler<HTMLInputElement> = { e -> setInput(e.target.value) }

	return Pair(input, changeHandler)
}

data class UseDebounceInput(
	val input: String,
	val debouncedInput: String,
	val changeEventHandler: ChangeEventHandler<HTMLInputElement>,
)

fun useDebouncedInput(defaultValue: String = "", delay: Duration = 300.milliseconds): UseDebounceInput {
	val (input, changeHandler) = useInput(defaultValue)
	val (debouncedTerm, setDebouncedTerm) = useState(input)

	useEffect(input, delay) {
		val handler = setTimeout(delay) {
			setDebouncedTerm(input)
		}

		cleanup {
			clearTimeout(handler)
		}
	}

	return UseDebounceInput(input, debouncedTerm, changeHandler)
}