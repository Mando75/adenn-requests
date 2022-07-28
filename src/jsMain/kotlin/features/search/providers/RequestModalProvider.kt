package features.search.providers


import react.*


val RequestModalContext = createContext<StateInstance<Boolean>>()

val RequestModalProvider = FC<PropsWithChildren>("RequestModalProvider") { props ->
	// STATE
	val modalStateInstance = useState(false)

	// HOOKS

	// EFFECTS

	// RENDER

	RequestModalContext.Provider(modalStateInstance) {
		+props.children
	}
}