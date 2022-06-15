package support.extensions

import seskar.js.JsString
import seskar.js.JsUnion

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
