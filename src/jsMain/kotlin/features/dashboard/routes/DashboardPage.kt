package features.dashboard.routes


import features.dashboard.components.OpenRequests
import features.dashboard.components.RecentlyAdded
import react.FC
import react.Fragment
import react.Props

external interface IDashboardPageProps : Props {

}

val DashboardPage = FC<IDashboardPageProps>("DashboardPage") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	Fragment {
		OpenRequests()
		RecentlyAdded()
	}
}