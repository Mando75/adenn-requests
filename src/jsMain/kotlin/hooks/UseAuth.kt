package hooks

import components.config.SessionContext
import components.config.SessionState
import react.useContext


fun useAuth(): SessionState {
	return useContext(SessionContext)
}