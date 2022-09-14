package components.modal


import csstype.ClassName
import react.FC
import react.PropsWithChildren
import react.ReactNode
import react.dom.aria.ariaHidden
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.span
import wrappers.XMarkIcon

external interface IModalProps : PropsWithChildren {
	var title: String
	var show: Boolean
	var close: () -> Unit
	var actions: ReactNode?
}

val Modal = FC<IModalProps>("Modal") { props ->
	// STATE
	val hidden = when (props.show) {
		true -> ""
		false -> "hidden"
	}
	// HOOKS

	// EFFECTS

	// RENDER
	// Main modal
	div {
		tabIndex = -1
		ariaHidden = props.show
		className =
			ClassName(
				"""$hidden overflow-y-auto bg-gray-600/50 overflow-x-hidden 
				| fixed top-0 right-0 left-50 z-50 w-full h-full
				| flex justify-center""".trimMargin()
			)

		div {
			className = ClassName("relative p-4 w-full max-w-2xl h-full md:h-auto mt-40")
			/// Modal Content
			div {
				className = ClassName("relative bg-white rounded-lg shadow")
				/// Header
				div {
					className = ClassName("flex justify-between items-start p-4 rounded-t border-b")

					h3 {
						className = ClassName("text-xl font-semibold text-gray-900")

						+props.title
					}
					button {
						className =
							ClassName("text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center")
						onClick = { props.close() }

						XMarkIcon {
							ariaHidden = props.show
							className = ClassName("shrink h-5 w-5")
						}
						span {
							className = ClassName("sr-only")
							+"Close Modal"
						}
					}
				}
				/// Modal Body
				div {
					className = ClassName("p-6 space-y-6")

					+props.children
				}
				/// Modal footer
				div {
					className = ClassName("flex items-center p-6 space-x-2 rounded-b border-t border-gray-200")

					props.actions?.let { actions -> +actions }
				}
			}
		}
	}
}