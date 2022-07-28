package features.search.components


import components.modal.Modal
import features.search.providers.RequestModalContext
import react.FC
import react.Props
import react.useContext

external interface IRequestModalProps : Props {

}


val RequestModal = FC<IRequestModalProps>("RequestModal") { props ->
	// STATE
	val (showModal, setShowModal) = useContext(RequestModalContext)

	// HOOKS

	// EFFECTS

	// RENDER
	Modal {
		show = showModal
		close = { setShowModal(false) }
		title = "Details"

		+"Request Modal Body"
	}
}