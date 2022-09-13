package features.dashboard.components


import features.dashboard.api.useMyRequestsQuery
import react.FC
import react.Props

external interface IOpenRequestsProps : Props {

}

val MyRequests = FC<IOpenRequestsProps>("OpenRequests") { props ->
	/// STATE
	val myRequestsQuery = useMyRequestsQuery()

	/// HOOKS

	/// EFFECTS

	/// RENDER
	CarouselWrapper {
		queryResult = myRequestsQuery
		title = "Your Recent Requests"
		emptyMessage = "You have not submitted any requests"
	}
}