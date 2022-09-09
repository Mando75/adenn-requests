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

external interface IOpenRequestsProps : Props {

}

val OpenRequests = FC<IOpenRequestsProps>("OpenRequests") { props ->
	/// STATE
	val openRequests = listOf("Foo", "Bar", "Baz")

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		h2 {
			className = ClassName("text-4xl")
			+"Your Open Requests"
		}
		openRequests.isNotEmpty().render {
			ul {
				className = ClassName("flex flex-row flex-wrap gap-4")
				openRequests.map { request ->
					li {
						className = ClassName("p-12 rounded border-solid border border-sky-500")
						+request
					}
				}
			}
		}
		openRequests.isEmpty().render {
			p {
				className = ClassName("text-medium")
				+"You have no open requests"
			}
		}
	}
}