package hooks

import components.config.SessionContext
import components.config.SessionState
import react.StateInstance
import react.useContext


fun useAuth(): StateInstance<SessionState> {
	return useContext(SessionContext)
}