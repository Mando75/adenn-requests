package features.search.providers


import entities.MediaType
import react.*

sealed class RequestModalAction {
	object CloseModal : RequestModalAction()

	data class OpenModalWithMedia(val tmdbId: Int, val mediaType: MediaType, val title: String) : RequestModalAction()
}

data class RequestModalState(
	val open: Boolean,
	val tmdbId: Int? = null,
	val mediaType: MediaType? = null,
	val title: String? = null
)

private val requestModalReducer: Reducer<RequestModalState, RequestModalAction> = { state, action ->
	when (action) {
		is RequestModalAction.CloseModal -> state.takeIf { !state.open } ?: state.copy(open = false)
		is RequestModalAction.OpenModalWithMedia -> RequestModalState(
			open = true,
			tmdbId = action.tmdbId,
			mediaType = action.mediaType,
			title = action.title
		)
	}
}

val RequestModalContext = createContext<ReducerInstance<RequestModalState, RequestModalAction>>()

val RequestModalProvider = FC<PropsWithChildren>("RequestModalProvider") { props ->
	// STATE
	val reducer = useReducer(requestModalReducer, RequestModalState(false))

	// HOOKS

	// EFFECTS

	// RENDER

	RequestModalContext.Provider(reducer) {
		+props.children
	}
}
