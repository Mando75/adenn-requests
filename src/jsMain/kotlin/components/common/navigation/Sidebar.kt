package components.common.navigation

import csstype.ClassName
import hooks.useAuth
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.key

data class NavigationItem(val displayText: String, val target: String)

private val navigationItems = listOf(
	NavigationItem("Home", "/"),
	NavigationItem("Search", "/search"),
	NavigationItem("Requests", "/requests"),
	NavigationItem("Profile", "/user")
)

val Sidebar = FC<Props>("Sidebar") {
	val auth = useAuth()
	val authPath = auth.user?.let { "/user/logout" } ?: "/user/login"
	val authText = auth.user?.let { "Logout" } ?: "Login"

	div {
		className = ClassName("bg-white rounded p-3 shadow-lg h-full flex flex-col justify-between")
		div {
			div {
				className = ClassName("flex items-center space-x-4 p-2 mb-5")

				+"Avatar Zone"
			}
			ul {
				className = ClassName("space-y-2 text-sm")

				navigationItems.map { navItem ->
					li {
						key = navItem.displayText

						react.router.dom.Link {
							to = navItem.target
							className =
								ClassName("flex items-center space-x-3 text-gray-700 p-4 rounded-md font-medium hover:bg-gray-200 focus:bg-gray-200 focus:shadow-outline")

							+navItem.displayText
						}
					}
				}
			}
		}
		react.router.dom.Link {
			to = authPath
			className =
				ClassName("flex items-center space-x-3 text-gray-700 p-4 rounded-md font-medium hover:bg-gray-200 focus:bg-gray-200 focus:shadow-outline")

			+authText
		}
	}
}