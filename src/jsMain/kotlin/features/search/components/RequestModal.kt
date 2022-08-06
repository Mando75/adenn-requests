package features.search.components


import components.modal.Modal
import features.search.providers.RequestModalAction
import features.search.providers.RequestModalContext
import react.FC
import react.Props
import react.useContext

external interface IRequestModalProps : Props {

}


val RequestModal = FC<IRequestModalProps>("RequestModal") { props ->
	// STATE
	val (modalState, dispatchModal) = useContext(RequestModalContext)

	// HOOKS

	// EFFECTS

	// RENDER
	Modal {
		show = modalState.open
		close = { dispatchModal(RequestModalAction.CloseModal) }
		title = "Details"

		+"Request Modal Body"
	}
}