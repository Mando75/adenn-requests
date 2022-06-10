@file:JsModule("react-query/devtools")
@file:JsNonModule

package wrappers

external interface QueryDevToolsProps : react.Props {
	var initialIsOpen: Boolean
	var position: String?
}


external val ReactQueryDevtools: react.FC<QueryDevToolsProps>
