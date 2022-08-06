package features.search.providers


import entities.MediaType
import react.*

sealed class RequestModalAction {
	object OpenModal : RequestModalAction()

	object CloseModal : RequestModalAction()
	data class SetMedia(val tmdbId: Int, val mediaType: MediaType) : RequestModalAction()
}

data class RequestModalState(
	val open: Boolean,
	val tmdbId: Int? = null,
	val mediaType: MediaType? = null,
)

private val requestModalReducer: Reducer<RequestModalState, RequestModalAction> = { state, action ->
	when (action) {
		is RequestModalAction.OpenModal -> state.takeIf { state.open } ?: state.copy(open = true)
		is RequestModalAction.CloseModal -> state.takeIf { !state.open } ?: state.copy(open = false)
		is RequestModalAction.SetMedia -> state.takeIf { state.tmdbId == action.tmdbId && state.mediaType == action.mediaType }
			?: state.copy(tmdbId = action.tmdbId, mediaType = action.mediaType)
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
