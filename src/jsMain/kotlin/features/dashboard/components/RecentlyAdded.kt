package features.dashboard.components


import csstype.ClassName
import lib.render
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.ul

external interface IRecentlyAddedProps : Props {

}

val RecentlyAdded = FC<IRecentlyAddedProps>("RecentlyAdded") { props ->
	/// STATE
	val recentlyAdded = emptyList<String>()

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		h2 {
			className = ClassName("text-4xl")
			+"Recently Added"
		}
		recentlyAdded.isNotEmpty().render {
			ul {
				className = ClassName("flex flex-row flex-wrap gap-4")
				recentlyAdded.map { added ->
					li {
						className = ClassName("p-12 rounded border-solid border border-sky-500")
						+added
					}
				}
			}
		}
		recentlyAdded.isEmpty().render {
			p {
				className = ClassName("text-medium")
				+"Nothing to see here"
			}
		}
	}
}