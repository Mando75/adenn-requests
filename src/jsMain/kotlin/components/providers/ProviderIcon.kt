package components.providers


import csstype.ClassName
import entities.Provider
import react.FC
import react.Props
import react.dom.html.ReactHTML.img

external interface IProviderProps : Props {
	var provider: Provider
}

val ProviderIcon = FC<IProviderProps>("ProviderIcon") { props ->
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	img {
		src = props.provider.logoPath.value
		alt = props.provider.name
		title = props.provider.name

		className = ClassName("rounded w-12 h-12")
	}
}