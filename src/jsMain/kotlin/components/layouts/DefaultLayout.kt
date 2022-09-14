package components.layouts

import components.navigation.Navigation
import csstype.ClassName
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import wrappers.ReactQueryDevtools


val DefaultLayout = FC<PropsWithChildren>("DefaultLayout") { props ->
	div {
		className = ClassName("grid grid-cols-12  gap-4 min-h-screen")

		Navigation()
		main {
			className = ClassName("md:col-span-12 lg:col-span-11 mb-4 mr-4 mt-6 px-4")

			+props.children

			ReactQueryDevtools {
				initialIsOpen = false
				position = "bottom-right"
			}
		}

	}
}