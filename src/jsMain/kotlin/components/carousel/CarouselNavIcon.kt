package components.carousel


import csstype.ClassName
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.PropsWithChildren
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.button

external interface ICarouselNavIconProps : PropsWithChildren {
	var className: ClassName?
	var onClick: MouseEventHandler<HTMLButtonElement>?
}

val CarouselNavIcon = FC<ICarouselNavIconProps>("CarouselNavIcon") { props ->
	/// STATE

	/// HOOKS

	/// EFFECTS

	/// RENDER
	button {
		onClick = props.onClick
		className = ClassName(
			"""rounded-full border-slate-300 border border-solid 
			| bg-white text-xs w-16 h-16 flex justify-center items-center 
			| absolute top-1/2 -translate-y-1/2 ${props.className}
		""".trimMargin()
		)
		+props.children
	}
}