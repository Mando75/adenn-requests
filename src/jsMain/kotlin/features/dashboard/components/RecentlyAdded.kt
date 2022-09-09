package features.dashboard.components


import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2

external interface IRecentlyAddedProps : Props {

}

val RecentlyAdded = FC<IRecentlyAddedProps>("RecentlyAdded") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	h2 {
		className = ClassName("text-4xl")
		+"Recently Added"
	}
}