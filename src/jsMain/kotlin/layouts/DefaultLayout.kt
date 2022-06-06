package layouts

import components.common.navigation.Sidebar
import csstype.ClassName
import react.FC
import react.Fragment
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.nav
import react.router.Outlet
import wrappers.ReactQueryDevtools


val DefaultLayout = FC<PropsWithChildren> {
	div {
		className = ClassName("grid grid-cols-12 gap-4 min-h-screen")

		Fragment {
			nav {
				className = ClassName("col-span-2")

				Sidebar()
			}

			main {
				className = ClassName("col-span-10")
				Outlet()
				ReactQueryDevtools {
					initialIsOpen = false
					position = "bottom-right"
				}
			}
		}

	}
}