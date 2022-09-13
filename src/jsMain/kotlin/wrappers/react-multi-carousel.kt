@file:JsModule("react-multi-carousel")
@file:JsNonModule
@file:Suppress("unused")

package wrappers

import csstype.ClassName
import react.*

external interface Responsive {
	var desktop: ResponsiveItem
	var tablet: ResponsiveItem
	var mobile: ResponsiveItem
}

external interface ResponsiveItem {
	var breakpoint: Breakpoint
	var items: Int
	var partialVisibilityGutter: Int?
	var slidesToSlide: Int?
}

external interface Breakpoint {
	var max: Int
	var min: Int
}

external interface CarouselProps : PropsWithChildren {
	var additionalTransform: Int?
	var afterChange: ((previousSlide: Int, state: CarouselInternalState) -> Unit)?
	var arrows: Boolean?
	var autoPlay: Boolean?
	var autoPlaySpeed: Int?
	var beforeChange: ((nextSlide: Int, state: CarouselInternalState) -> Unit)?
	var centerMode: Boolean?
	var className: ClassName?
	var containerClass: ClassName?
	var customButtonGroup: ReactElement<Props>?
	var customDot: ReactElement<Props>?
	var customLeftArrow: ReactElement<Props>?
	var customRightArrow: ReactElement<Props>?
	var customTransition: String?
	var deviceType: String?
	var dotListClass: ClassName?
	var draggable: Boolean?
	var focusOnSelect: Boolean?
	var infinite: Boolean?
	var itemAriaLabel: String?
	var itemClass: ClassName?
	var keyBoardControl: Boolean?
	var minimumTouchDrag: Int?
	var partialVisible: Boolean?
	var pauseOnHover: Boolean?
	var removeArrowOnDeviceType: Array<String>
	var renderArrowsWhenDisabled: Boolean?
	var renderButtonGroupOutside: Boolean?
	var renderDotsOutside: Boolean?
	var responsive: Responsive
	var rewind: Boolean?
	var rewindWithAnimation: Boolean?
	var rtl: Boolean?
	var shouldResetAutoplay: Boolean?
	var showDots: Boolean?
	var sliderClass: ClassName?
	var slidesToSlide: Int?
	var ssr: Boolean?
	var swipeable: Boolean?
	var transitionDuration: Int?
}

external interface CarouselInternalState : State {
	var itemWidth: Int
	var containerWidth: Int
	var slidesToShow: Int
	var currentSlide: Int
	var totalItems: Int
	var domLoaded: Boolean
	var deviceType: String?
	var transform: Int
}

@JsName("default")
external val Carousel: FC<CarouselProps>
