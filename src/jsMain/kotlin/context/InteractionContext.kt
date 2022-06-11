package context

import hooks.useInteraction
import react.FC
import react.PropsWithChildren
import react.createContext
import react.useContext

value class InteractionContextValue(val isTouch: Boolean)

private val InteractionContext = createContext(InteractionContextValue(false))

val InteractionProvider = FC<PropsWithChildren> { props ->
	val isTouch = useInteraction()

	InteractionContext.Provider(value = InteractionContextValue(isTouch)) {
		props.children
	}
}

fun useIsTouch(): Boolean = useContext(InteractionContext).isTouch
