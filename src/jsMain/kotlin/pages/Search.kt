package pages

import components.search.SearchInput
import csstype.ClassName
import middleware.RequireAuth
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.section


private val Search = FC<Props>("SearchPage") {

	section {
		h1 {
			className = ClassName("text-4xl")
			+"Search for Content"
		}
		SearchInput()
	}
}


val SearchPage = RequireAuth.create() {
	Search()
}