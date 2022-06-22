package lib.reactTransitionState

import seskar.js.JsString
import seskar.js.JsUnion

@Suppress("UNUSED")
@JsUnion
external enum class TransitionState {
	@JsString("preEnter")
	PRE_ENTER,

	@JsString("entering")
	ENTERING,

	@JsString("entered")
	ENTERED,

	@JsString("preExit")
	PRE_EXIT,

	@JsString("exiting")
	EXITING,

	@JsString("exited")
	EXITED,

	@JsString("unmounted")
	UNMOUNTED
}
