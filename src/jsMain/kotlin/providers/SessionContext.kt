package providers

import entities.UserEntity
import features.auth.api.useMeQuery
import kotlinx.js.jso
import react.*
import react.router.useNavigate


data class SessionState(val user: UserEntity?, val loading: Boolean)

private val SessionContext = createContext<StateInstance<SessionState>>()

val SessionProvider = FC<PropsWithChildren>("SessionManager") { props ->
	val navigate = useNavigate()
	val query = useMeQuery()
	val sessionStateInstance = useState(SessionState(null, query.isLoading))
	val (_, setSessionState) = sessionStateInstance
	query.refetch

	useEffect(query.isLoading, query.isError) {
		if (!query.isError) {
			setSessionState(SessionState(query.data, query.isLoading))
		} else {
			setSessionState(SessionState(null, query.isLoading))
			navigate("/login", jso { replace = true })
		}
	}
	SessionContext.Provider(sessionStateInstance) {
		+props.children
	}
}

fun useSession(): StateInstance<SessionState> {
	return useContext(SessionContext)
}