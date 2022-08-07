package features.search.providers


import entities.SearchResultEntity
import react.*

sealed class RequestModalAction {
	object CloseModal : RequestModalAction()

	data class OpenModalWithMedia(val searchResult: SearchResultEntity) : RequestModalAction()
}

data class RequestModalState(
	val open: Boolean, val searchResult: SearchResultEntity? = null
)

private val requestModalReducer: Reducer<RequestModalState, RequestModalAction> = { state, action ->
	when (action) {
		is RequestModalAction.CloseModal -> state.takeIf { !state.open } ?: RequestModalState(
			open = false, searchResult = null
		)

		is RequestModalAction.OpenModalWithMedia -> RequestModalState(
			open = true, searchResult = action.searchResult
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
