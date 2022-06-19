package components.navigation

import csstype.ClassName
import providers.useSession
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.key

data class NavigationItem(val displayText: String, val target: String)

private val protectedItems = listOf(
	NavigationItem("Home", "/"),
	NavigationItem("Search", "/search"),
	NavigationItem("Requests", "/requests"),
	NavigationItem("Profile", "/profile")
)

private val publicItems = listOf(
	NavigationItem("Login", "/auth/login"),
)

val Sidebar = FC<Props>("Sidebar") {
	val (auth) = useSession()
	val items = auth.user?.let { protectedItems } ?: publicItems

	div {
		className = ClassName("bg-white rounded p-3 shadow-lg h-full flex flex-col justify-between")
		div {
			div {
				className = ClassName("flex items-center space-x-4 p-2 mb-5")

				+"Plex Requests"
			}
			ul {
				className = ClassName("space-y-2 text-sm")

				items.map { navItem ->
					li {
						key = navItem.displayText

						react.router.dom.Link {
							to = navItem.target
							className =
								ClassName(
									"""
									| flex items-center space-x-3 p-4 rounded-md 
									| text-gray-700 font-medium 
									| hover:bg-gray-200 focus:bg-gray-200 focus:shadow-outline
									|""".trimMargin()
								)

							+navItem.displayText
						}
					}
				}
			}
		}
		auth.user?.let {
			react.router.dom.Link {
				to = "/profile/logout"
				className =
					ClassName(
						"""flex items-center space-x-3 p-4 rounded-md 
					| text-gray-700 font-medium 
					| hover:bg-gray-200 focus:bg-gray-200 focus:shadow-outline""".trimMargin()
					)

				+"Logout"
			}
		}
	}
}