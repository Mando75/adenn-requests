package layouts

import components.navigation.Sidebar
import csstype.ClassName
import react.FC
import react.Fragment
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.nav
import react.router.Outlet
import wrappers.ReactQueryDevtools


val DefaultLayout = FC<PropsWithChildren>("DefaultLayout") {
	div {
		className = ClassName("grid grid-cols-12 gap-4 min-h-screen")

		Fragment {
			nav {
				className = ClassName("col-span-2")

				Sidebar()
			}

			main {
				className = ClassName("col-span-10 mb-4 mr-4 mt-6")
				Outlet()
				ReactQueryDevtools {
					initialIsOpen = false
					position = "bottom-right"
				}
			}
		}

	}
}