package components.providers


import csstype.ClassName
import react.FC
import react.Props
import entities.Provider
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
		className = ClassName("flex flex-row")

		props.providers.map { p -> ProviderIcon { provider = p} }
	}
}