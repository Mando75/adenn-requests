package features.search.components


import components.button.Button
import components.modal.Modal
import components.providers.ProviderList
import components.spinners.BarsScaleMiddle
import csstype.ClassName
import entities.toMediaType
import features.search.api.useMediaProvidersQuery
import features.search.api.useSubmitRequestMutation
import features.search.providers.RequestModalAction
import features.search.providers.RequestModalContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import lib.reactQuery.exec
import org.w3c.dom.HTMLButtonElement
import react.*
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span

external interface IRequestModalProps : Props {

}


val RequestModal = FC<IRequestModalProps>("RequestModal") { _ ->
	/// HOOKS
	val (modalState, dispatchModal) = useContext(RequestModalContext)
	val searchResult = modalState.searchResult

	val mediaProvidersQuery = useMediaProvidersQuery(searchResult?.id, searchResult?.toMediaType())
	val submitRequestMutation = useSubmitRequestMutation()
	/// STATE
	val isEmpty = !mediaProvidersQuery.isLoading && mediaProvidersQuery.data?.isEmpty() ?: true

	val modalTitle = if (mediaProvidersQuery.isLoading) {
		"Checking Providers..."
	} else if (isEmpty) {
		"Submitting Request"
	} else {
		"Available Providers"
	}

	val submitRequest: MouseEventHandler<HTMLButtonElement> = useCallback(searchResult, submitRequestMutation) {
		searchResult?.let { result ->
			MainScope().promise {
				submitRequestMutation.exec(result)
				dispatchModal(RequestModalAction.CloseModal)
			}
		}
	}
	/// EFFECTS

	/// RENDER
	Modal {
		show = modalState.open
		close = { dispatchModal(RequestModalAction.CloseModal) }
		title = modalTitle

		div {
			className = ClassName("w-full")
			if (mediaProvidersQuery.isLoading) {
				div {
					className = ClassName("w-full flex justify-center")
					BarsScaleMiddle()
				}
			}
			if (isEmpty) {
				p { +"No Providers" }
			} else {
				p {
					className = ClassName("mb-4")
					span {
						className = ClassName("font-semibold")

						searchResult?.let { result -> +result.title }
					}
					+" is available on the following providers:"
				}
			}
			mediaProvidersQuery.data?.let { providerList ->
				div {
					ProviderList { providers = providerList }
				}
			}
		}

		actions = Fragment.create() {
			div {
				className = ClassName("w-full flex flex-row justify-end gap-4")
				Button {
					onClick = { dispatchModal(RequestModalAction.CloseModal) }
					className = ClassName(
						"""
						| bg-white text-red-700 border border-red-700 
					    | focus:ring-red-800 focus:bg-red-300/50 hover:bg-red-300/50
					""".trimMargin()
					)
					+"Cancel"
				}

				Button {
					onClick = submitRequest

					if (submitRequestMutation.isLoading) {
						BarsScaleMiddle {
							className = ClassName("fill-white")
						}
					} else {
						+"Request"
					}
				}
			}
		}
	}
}