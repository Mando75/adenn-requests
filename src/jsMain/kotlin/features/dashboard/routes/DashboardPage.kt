package features.dashboard.routes


import csstype.ClassName
import features.dashboard.components.OpenRequests
import features.dashboard.components.Popular
import features.dashboard.components.RecentlyAdded
import react.FC
import react.Props
import react.dom.html.ReactHTML.section

external interface IDashboardPageProps : Props {

}

val DashboardPage = FC<IDashboardPageProps>("DashboardPage") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	section {
		className = ClassName("grid grid-rows-3 gap-4")

		OpenRequests()
		RecentlyAdded()
		Popular()
	}
}