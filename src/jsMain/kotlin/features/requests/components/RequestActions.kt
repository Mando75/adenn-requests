package features.requests.components


import components.button.Button
import csstype.ClassName
import features.requests.api.UpdateRequestStatusMutationVariables
import features.requests.api.useUpdateRequestStatusMutation
import features.requests.hooks.useAllowedActions
import lib.reactQuery.exec
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.key

external interface IRequestActionsProps : Props {
	var requestId: Int
}

val RequestActions = FC<IRequestActionsProps>("RequestActions") { props ->
	/// HOOKS
	val allowedActions = useAllowedActions()
	val updateStatusMutation = useUpdateRequestStatusMutation()
	/// STATE

	/// EFFECTS

	/// RENDER
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
								target = e.currentTarget
							)
						)
					}
					loading = updateStatusMutation.isLoading

					+action.label
				}
			}
		}
	}
}