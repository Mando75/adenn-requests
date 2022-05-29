package hooks

import org.w3c.dom.HTMLInputElement
import react.dom.events.ChangeEventHandler
import react.useCallback
import react.useState

fun useInput(defaultValue: String = ""): Pair<String, ChangeEventHandler<HTMLInputElement>> {
	val inputState = useState<String>(defaultValue)
	val (input, setInput) = inputState
	val changeHandler = useCallback<ChangeEventHandler<HTMLInputElement>>(input) { e -> setInput(e.target.value) }

	return Pair(input, changeHandler)
}