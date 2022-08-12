package features.search.components

import components.attribution.Attribution
import components.button.Button
import csstype.ClassName
import org.w3c.dom.HTMLInputElement
import react.FC
import react.PropsWithChildren
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaLabel
import react.dom.events.ChangeEventHandler
import react.dom.events.MouseEventHandler
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import wrappers.XIcon

external interface SearchInputProps : PropsWithChildren {
	var value: String
	var onChange: ChangeEventHandler<HTMLInputElement>?
	var onClick: MouseEventHandler<*>?
	var onClear: MouseEventHandler<*>?
	var placeholder: String?
}

val SearchInput = FC<SearchInputProps>("SearchInput") { props ->
	div {
		className = ClassName("flex justify-center")
		div {
			className = ClassName("mb-3 w-4/5 flex flex-col")
			div {
				className = ClassName("input-group relative flex items-stretch w-full mb-4")
				input {
					autoFocus = true
					value = props.value
					onChange = props.onChange
					type = InputType.search
					placeholder = props.placeholder ?: "Search for Content"
					ariaLabel = "Search"
					ariaDescribedBy = "button-trigger-search"
					className = ClassName(
						"""
						| form-control relative flex-auto min-w-0 block 
						| w-full px-3 py-1.5 text-base font-normal 
						| text-gray-700 bg-white bg-clip-padding border border-r-0 
						| border-solid border-gray-300 rounded-l rounded-r-none transition 
						| ease-in-out m-0 focus:text-gray-700 focus:bg-white 
						| focus:border-blue-600 focus:outline-none peer 
					""".trimMargin()
					)

				}
				Button {
					onClick = props.onClear
					type = ButtonType.button
					id = "button-clear-search"
					className =
						ClassName(
							"""py-1 px-1 w-10 flex justify-center rounded-none bg-white 
							| border border-y-gray-300 border-x-white text-gray-700  
							| hover:bg-white focus:bg-white focus:border-blue-600 focus:ring-0 
							| peer-focus:border-y-blue-600""".trimMargin()
						)

					XIcon()

				}
				Button {
					onClick = props.onClick
					type = ButtonType.button
					id = "button-trigger-search"
					className = ClassName("rounded-l-none")

					+"Search"
				}
			}
			Attribution()
		}
	}
}