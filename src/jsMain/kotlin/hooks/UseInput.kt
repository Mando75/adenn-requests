package hooks

import kotlinx.js.timers.clearTimeout
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLInputElement
import react.dom.events.ChangeEventHandler
import react.useEffect
import react.useState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class UseInput(
	val input: String,
	val changeHandler: ChangeEventHandler<HTMLInputElement>,
	val clearHandler: () -> Unit
)

fun useInput(defaultValue: String = ""): UseInput {
	val (input, setInput) = useState(defaultValue)
	val changeHandler: ChangeEventHandler<HTMLInputElement> = { e -> setInput(e.target.value) }
	val clearHandler = { setInput("") }

	return UseInput(input, changeHandler, clearHandler)
}

data class UseDebounceInput(
	val input: String,
	val debouncedInput: String,
	val changeEventHandler: ChangeEventHandler<HTMLInputElement>,
	val clearHandler: () -> Unit
)

fun useDebouncedInput(defaultValue: String = "", delay: Duration = 300.milliseconds): UseDebounceInput {
	val (input, changeHandler, clearHandler) = useInput(defaultValue)
	val (debouncedTerm, setDebouncedTerm) = useState(input)

	useEffect(input, delay) {
		val handler = setTimeout(delay) {
			setDebouncedTerm(input)
		}

		cleanup {
			clearTimeout(handler)
		}
	}

	return UseDebounceInput(input, debouncedTerm, changeHandler, clearHandler)
}