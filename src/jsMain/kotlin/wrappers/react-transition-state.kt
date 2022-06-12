@file:JsModule("react-transition-state")
@file:JsNonModule
@file:Suppress("unused")

package wrappers

import support.extensions.JsTriple

external interface Timeout {
	var enter: Number?
	var exit: Number?
}

external interface TransitionEvent {
	var state: String
}

external interface TransitionOptions {
	var initialEntered: Boolean?
	var mountOnEnter: Boolean?
	var unmountOnExit: Boolean?
	var preEnter: Boolean?
	var preExit: Boolean?
	var enter: Boolean?
	var exit: Boolean?
	var timeout: Timeout?
	var onChange: ((event: TransitionEvent) -> Unit)?
}

@JsName("default")
external fun useTransition(options: TransitionOptions? = definedExternally): JsTriple<String, (toEnter: Boolean?) -> Unit, () -> Unit> // State, toggle, endTransition
