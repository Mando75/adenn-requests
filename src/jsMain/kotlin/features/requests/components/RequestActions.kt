package features.requests.components


import components.button.Button
import csstype.ClassName
import features.requests.api.UpdateRequestStatusMutationVariables
import features.requests.api.useUpdateRequestStatusMutation
import features.requests.hooks.useAllowedActions
import lib.reactQuery.exec
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.textarea
import react.dom.html.ReactHTML.ul
import react.key
import react.useState

external interface IRequestActionsProps : Props {
	var requestId: Int
}

val RequestActions = FC<IRequestActionsProps>("RequestActions") { props ->
	/// HOOKS
	val (allowedActions, showRejectReason) = useAllowedActions()
	val updateStatusMutation = useUpdateRequestStatusMutation()
	/// STATE
	val (rejectionReason, setRejectionReason) = useState("")

	/// EFFECTS

	/// RENDER
	div {
		className = ClassName("w-full")
		ul {
			className = ClassName("w-full")
			allowedActions.map { action ->
				li {
					key = action.value.toString()
					className = ClassName("mt-2")

					Button {
						disabled = updateStatusMutation.isLoading
						className = ClassName("w-full ${action.color().allClassName()}")
						onClick = { e ->
							updateStatusMutation.exec(
								UpdateRequestStatusMutationVariables(
									requestId = props.requestId,
									status = action.value,
									target = e.currentTarget,
									rejectionReason = rejectionReason
								)
							)
						}
						loading = updateStatusMutation.isLoading

						+action.label
					}
				}
			}
		}
		if (showRejectReason) {
			textarea {
				name = "rejectionreason"
				title = "Rejection Reason"
				value = rejectionReason
				className = ClassName(
					"""mt-4 p-2 w-full text-gray-900 bg-gray-50 rounded-lg border border-gray-300 
				| outline-none focus:outline-none focus-visible:outline-none
				| focus:ring-blue-500 focus:border-blue-500 focus-visible:ring-blue-500 focus:border-blue-500 
			""".trimMargin()
				)
				onChange = { e -> setRejectionReason(e.currentTarget.value) }
			}
		}
	}
}