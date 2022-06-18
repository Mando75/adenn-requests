package features.profile.routes

import csstype.ClassName
import features.profile.api.CreateAuthTokenVariables
import features.profile.api.useCreateAuthTokenMutation
import lib.ReactQuery.exec
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section

val TokenPage = FC<Props>("TokenPage") {
	val tokenMutation = useCreateAuthTokenMutation()

	section {
		className = ClassName("mt-4")

		div {
			if (tokenMutation.isLoading) {
				+"Loading"
			} else if (tokenMutation.isError) {
				+"Error: ${tokenMutation.error}"
			} else if (tokenMutation.isSuccess) {
				p { +"Success: ${tokenMutation.data?.token}" }
			}

		}

		button {
			+"Create Token"
			onClick = { tokenMutation.exec(CreateAuthTokenVariables()) }
		}
	}
}
