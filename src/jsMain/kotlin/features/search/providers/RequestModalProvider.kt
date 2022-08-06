package features.search.providers


import entities.MediaType
import react.*

data class RequestModalState(
	val open: Boolean,
	val setOpen: StateSetter<Boolean>,
	val mediaId: Int? = null,
	val setMediaId: StateSetter<Int?>,
	val mediaType: MediaType? = null,
	val setMediaType: StateSetter<MediaType?>
)

val RequestModalContext = createContext<RequestModalState>()

val RequestModalProvider = FC<PropsWithChildren>("RequestModalProvider") { props ->
	// STATE
	val (open, setOpen) = useState(false)
	val (mediaId, setMediaId) = useState<Int?>(null)
	val (mediaType, setMediaType) = useState<MediaType?>(null)

	val modalStateInstance = RequestModalState(open, setOpen, mediaId, setMediaId, mediaType, setMediaType)

	// HOOKS

	// EFFECTS

	// RENDER

	RequestModalContext.Provider(modalStateInstance) {
		+props.children
	}
}
