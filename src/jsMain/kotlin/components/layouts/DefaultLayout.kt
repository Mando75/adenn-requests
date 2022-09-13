package components.layouts

import components.navigation.Sidebar
import csstype.ClassName
import react.FC
import react.Fragment
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.nav
import wrappers.ReactQueryDevtools


val DefaultLayout = FC<PropsWithChildren>("DefaultLayout") { props ->
	div {
		className = ClassName("grid grid-cols-12  gap-4 min-h-screen")

		Fragment {
			nav {
				className = ClassName("col-span-1")

				Sidebar()
			}

			main {
				className = ClassName("col-span-11 mb-4 mr-4 mt-6 px-4")

				+props.children

				ReactQueryDevtools {
					initialIsOpen = false
					position = "bottom-right"
				}
			}
		}

	}
}