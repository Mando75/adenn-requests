package components.config


import react.FC
import react.PropsWithChildren
import react.router.useLocation

val DebugRouter = FC<PropsWithChildren>("DebugRouter") { props ->
	// STATE
	val location = useLocation()
	console.log("Location:")
	console.dir(location)

	// HOOKS

	// EFFECTS

	// RENDER
	+props.children
}