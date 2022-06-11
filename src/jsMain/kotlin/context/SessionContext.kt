package context

import api.queries.useMeQuery
import entities.UserEntity
import kotlinx.js.jso
import react.*
import react.router.useNavigate


data class SessionState(val user: UserEntity?)

private val SessionContext = createContext<StateInstance<SessionState>>()

val SessionProvider = FC<PropsWithChildren>("SessionManager") { props ->
	val navigate = useNavigate()
	val query = useMeQuery()
	val sessionStateInstance = useState(SessionState(null))
	val (_, setSessionState) = sessionStateInstance
	query.refetch

	useEffect(query.isLoading, query.isError) {
		if (!query.isError) {
			setSessionState(SessionState(query.data))
		} else {
			setSessionState(SessionState(null))
			navigate("/login", jso { replace = true })
		}
	}
	SessionContext.Provider(sessionStateInstance) {
		if (query.isLoading || query.isFetching) {
			+"Loading..."
		} else {
			+props.children
		}
	}
}

fun useSession(): StateInstance<SessionState> {
	return useContext(SessionContext)
}