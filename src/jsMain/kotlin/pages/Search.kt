package pages

import components.search.SearchInput
import csstype.ClassName
import hooks.useInput
import middleware.RequireAuth
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section


private val Search = FC<Props>("SearchPage") {

	val (searchTerm, onSearchChange) = useInput()

	section {
		className = ClassName("mt-4")
		SearchInput {
			value = searchTerm
			onChange = onSearchChange
		}
		p {
			+searchTerm
		}
	}
}


val SearchPage = RequireAuth.create {
	Search()
}