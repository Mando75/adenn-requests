package features.search.components


import components.modal.Modal
import components.providers.ProviderList
import components.spinners.BarsScaleMiddle
import csstype.ClassName
import features.search.api.useMediaProvidersQuery
import features.search.providers.RequestModalAction
import features.search.providers.RequestModalContext
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useContext

external interface IRequestModalProps : Props {

}


val RequestModal = FC<IRequestModalProps>("RequestModal") { props ->
	// HOOKS
	val (modalState, dispatchModal) = useContext(RequestModalContext)
	val query = useMediaProvidersQuery(modalState.tmdbId, modalState.mediaType)
	// STATE
	val isEmpty = !query.isLoading && query.data?.isEmpty() ?: true

	val modalTitle = if (query.isLoading) {
		"Checking Providers..."
	} else if (isEmpty) {
		"Submitting Request"
	} else {
		"Available Providers"
	}
	// EFFECTS

	// RENDER
	Modal {
		show = modalState.open
		close = { dispatchModal(RequestModalAction.CloseModal) }
		title = modalTitle

		div {
			className = ClassName("w-full")
			if (query.isLoading) {
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

						modalState.title?.let { title -> +title }
					}
					+" is available on the following providers:"
				}
			}
			query.data?.let { providerList ->
				div {
					ProviderList { providers = providerList }
				}
			}
		}
	}
}