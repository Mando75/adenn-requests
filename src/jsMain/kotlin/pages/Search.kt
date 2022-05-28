package pages

import csstype.ClassName
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
		+"Search Page"
	}
}


val SearchPage = Search.create()