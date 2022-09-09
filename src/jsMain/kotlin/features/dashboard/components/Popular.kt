package features.dashboard.components


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section

external interface IPopularProps : Props {

}

val Popular = FC<IPopularProps>("Popular") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		p {
			className = ClassName("text-4xl")
			+"Popular"
		}
	}
}