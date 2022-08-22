package components.button


import components.spinners.BarsScaleMiddle
import csstype.ClassName
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.PropsWithChildren
import react.dom.html.ButtonHTMLAttributes
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div

external interface IButtonProps : PropsWithChildren, ButtonHTMLAttributes<HTMLButtonElement> {
	var loading: Boolean?
	var loadingClass: ClassName?
}

val Button = FC<IButtonProps>("Button") { props ->
	/// RENDER
	button {
		// pass all inherited props to button
		+props

		className = ClassName(
			"""inline-block px-6 py-2 rounded bg-blue-600 
			| text-white font-medium text-xs leading-tight uppercase 
			| focus:ring-1 focus:ring-blue-800 focus:bg-blue-700 hover:bg-blue-700 
			| transition duration-150 ease-in-out
			| ${props.className}""".trimMargin()
		)

		if (props.loading == true) {
			div {
				className = ClassName("w-full flex content-center justify-center")

				BarsScaleMiddle {
					className = props.loadingClass ?: ClassName("fill-white")
					height = 15.0
				}
			}
		} else {
			+props.children
		}
	}
}