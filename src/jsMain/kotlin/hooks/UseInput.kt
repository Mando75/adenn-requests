package hooks

import kotlinx.js.timers.clearTimeout
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLInputElement
import react.dom.events.ChangeEventHandler
import react.useCallback
import react.useEffect
import react.useState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun useInput(defaultValue: String = ""): Pair<String, ChangeEventHandler<HTMLInputElement>> {
	val (input, setInput) = useState(defaultValue)
	val changeHandler = useCallback<ChangeEventHandler<HTMLInputElement>>(input) { e -> setInput(e.target.value) }

	return Pair(input, changeHandler)
}

data class UseDebounceInput(
	val input: String,
	val debouncedInput: String,
	val changeEventHandler: ChangeEventHandler<HTMLInputElement>,
	val loading: Boolean,
)

fun useDebouncedInput(defaultValue: String = "", delay: Duration = 300.milliseconds): UseDebounceInput {
	val (input, changeHandler) = useInput(defaultValue)
	val (debouncedTerm, setDebouncedTerm) = useState(input)
	val (loading, setLoading) = useState(false)

	useEffect(input, delay) {
		if (input.isNotBlank()) {
			setLoading(true)
		}
		val handler = setTimeout(delay) {
			setDebouncedTerm(input)
			setLoading(false)
		}

		cleanup {
			clearTimeout(handler)
		}
	}

	return UseDebounceInput(input, debouncedTerm, changeHandler, loading)
}