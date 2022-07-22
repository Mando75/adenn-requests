package features.requests.hooks

import arrow.core.NonEmptyList
import entities.RequestFilterMediaType
import entities.RequestFilters
import entities.RequestStatus
import hooks.useDebouncedInput
import org.w3c.dom.HTMLInputElement
import react.Reducer
import react.dom.events.ChangeEventHandler
import react.useEffect
import react.useReducer

sealed class RequestFiltersAction {
	data class SetStatus(
		val status: NonEmptyList<RequestStatus>,
	) : RequestFiltersAction()

	object ClearStatus : RequestFiltersAction()
	data class SetMediaType(
		val mediaType: RequestFilterMediaType
	) : RequestFiltersAction()

	object ClearMediaType : RequestFiltersAction()
	data class SetSearchTerm(val searchTerm: String?) : RequestFiltersAction()
}

private val requestFiltersReducer: Reducer<RequestFilters, RequestFiltersAction> = { state, action ->
	when (action) {
		is RequestFiltersAction.SetStatus -> state.takeIf { state.status == action.status }
			?: state.copy(status = action.status)
		is RequestFiltersAction.ClearStatus -> state.copy(status = null)
		is RequestFiltersAction.ClearMediaType -> state.copy(mediaType = RequestFilterMediaType.ALL)
		is RequestFiltersAction.SetMediaType -> state.takeIf { state.mediaType == action.mediaType } ?: state.copy(
			mediaType = action.mediaType
		)
		is RequestFiltersAction.SetSearchTerm -> state.takeIf { state.searchTerm == action.searchTerm } ?: state.copy(
			searchTerm = action.searchTerm
		)
	}
}

data class UseRequestFilters(
	val requestFilters: RequestFilters,
	val searchTerm: String,
	val changeHandler: ChangeEventHandler<HTMLInputElement>
)

fun useRequestFilters(): UseRequestFilters {
	val (searchTerm, debouncedSearchTerm, changeHandler) = useDebouncedInput()
	val (requestFilter, dispatch) = useReducer(requestFiltersReducer, RequestFilters(searchTerm = debouncedSearchTerm))

	useEffect(debouncedSearchTerm, dispatch, requestFilter) {
		if (debouncedSearchTerm !== requestFilter.searchTerm) {
			dispatch(RequestFiltersAction.SetSearchTerm(debouncedSearchTerm))
		}
	}

	return UseRequestFilters(requestFilter, searchTerm, changeHandler)
}