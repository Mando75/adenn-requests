package wrappers

import react.FC
import react.Props
import react.ReactElement

class ReactQueryDevToolsOption(val initialIsOpen: Boolean = true) // ???

interface QueryError {
	val message: String
}

@JsModule("react-query/devtools")
@JsNonModule
external val ReactQueryDevtools: dynamic

val reactQueryDevtools: (options: dynamic) -> ReactElement<Props> = ReactQueryDevtools.ReactQueryDevtools

fun cReactQueryDevtools(options: ReactQueryDevToolsOption = ReactQueryDevToolsOption()) =
	FC("ReactQueryDevtools") { _: Props ->
		child(reactQueryDevtools(options))
	}
