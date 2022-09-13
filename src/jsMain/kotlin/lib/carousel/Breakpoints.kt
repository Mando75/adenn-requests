package lib.carousel

import kotlinx.js.jso
import wrappers.Responsive
import wrappers.ResponsiveItem

val CarouselBreakpoints = object : Responsive {
	override var desktop: ResponsiveItem = jso {
		breakpoint = jso {
			max = 3000
			min = 1024
		}
		items = 5
	}

	override var tablet: ResponsiveItem = jso {
		breakpoint = jso {
			max = 1024
			min = 464
		}
		items = 3
	}

	override var mobile: ResponsiveItem = jso {
		breakpoint = jso {
			max = 464
			min = 0
		}
		items = 1
	}
}
