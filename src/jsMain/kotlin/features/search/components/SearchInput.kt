package features.search.components

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
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input

external interface SearchInputProps : PropsWithChildren {
	var value: String
	var onChange: ChangeEventHandler<HTMLInputElement>?
	var onClick: MouseEventHandler<*>?
	var placeholder: String?
}

val SearchInput = FC<SearchInputProps> { props ->
	div {
		className = ClassName("flex justify-center")
		div {
			className = ClassName("mb-3 w-4/5")
			div {
				className = ClassName("input-group relative flex items-stretch w-full mb-4")
				input {
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
						| text-gray-700 bg-white bg-clip-padding border 
						| border-solid border-gray-300 rounded-l rounded-r-none transition 
						| ease-in-out m-0 focus:text-gray-700 focus:bg-white 
						| focus:border-blue-600 focus:outline-none
					""".trimMargin()
					)

				}
				button {
					onClick = props.onClick
					type = ButtonType.button
					id = "button-trigger-search"
					className = ClassName(
						"""
						| btn inline-block px-6 py-2 rounded-l-none rounded-r bg-blue-600 
						| text-white font-medium text-xs leading-tight uppercase  
						| hover:bg-blue-700  
						| focus:ring-1 focus:ring-blue-800 focus:bg-blue-700
						| transition duration-150 ease-in-out
					""".trimMargin()
					)
					+"Search"
				}
			}
		}
	}
}