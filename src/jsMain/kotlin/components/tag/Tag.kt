package components.tag


import csstype.ClassName
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.span

sealed class TagStyle {
	abstract val internalClasses: String
	abstract val classes: String
}

data class PurpleTag(
	override val classes: String = "",
	override val internalClasses: String = "bg-purple-500"
) : TagStyle()

data class BlueTag(
	override val classes: String = "",
	override val internalClasses: String = "bg-blue-500"
) : TagStyle()

data class GreenTag(
	override val classes: String = "",
	override val internalClasses: String = "bg-green-500"
) : TagStyle()

data class RedTag(
	override val internalClasses: String = "bg-red-500",
	override val classes: String = ""
) : TagStyle()

data class CyanTag(
	override val internalClasses: String = "bg-cyan-500",
	override val classes: String = ""
) : TagStyle()

external interface TagProps : PropsWithChildren {
	var style: TagStyle
}

val Tag = FC<TagProps>("Tag") { props ->
	// STATE

	// HOOKS

	// EFFECTS

	// RENDER
	span {
		className =
			ClassName("text-white text-sm font-medium rounded-full px-2 py-1 ${props.style.internalClasses} ${props.style.classes}")
		+props.children
	}
}