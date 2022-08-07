package components.providers


import csstype.ClassName
import entities.Provider
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface IProviderListProps : Props {
	var providers: List<Provider>
}

val ProviderList = FC<IProviderListProps>("ProviderList") { props ->
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	div {
		className = ClassName("flex flex-row flex-wrap")

		props.providers.map { p -> ProviderIcon { provider = p } }
	}
}