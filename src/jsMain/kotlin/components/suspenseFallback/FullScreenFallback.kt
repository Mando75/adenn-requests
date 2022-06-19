package components.suspenseFallback


import components.spinners.BarsScaleMiddle
import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML

val FullScreenFallback = FC<Props>("FullScreenFallback") {
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	ReactHTML.div {
		className = ClassName("h-full, w-full flex items-center justify-center")
		BarsScaleMiddle()
	}
}