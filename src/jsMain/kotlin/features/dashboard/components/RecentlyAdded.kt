package features.dashboard.components


import features.dashboard.api.useRecentlyAddedQuery
import react.FC
import react.Props

external interface IRecentlyAddedProps : Props {

}

val RecentlyAdded = FC<IRecentlyAddedProps>("RecentlyAdded") { props ->
	/// STATE
	val recentlyAddedQuery = useRecentlyAddedQuery()

	/// HOOKS

	/// EFFECTS

	/// RENDER
	CarouselWrapper {
		queryResult = recentlyAddedQuery
		title = "Recently Added"
		emptyMessage = "Nothing to see here..."
	}
}