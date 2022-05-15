package components.config

import api.queries.useMeQuery
import entities.UserEntity
import kotlinx.js.jso
import react.*
import react.query.QueryObserverResult
import react.query.RefetchOptions
import react.router.useNavigate
import kotlin.js.Promise


typealias RefetchFunction<TQueryData, TError> = (options: RefetchOptions?) -> Promise<QueryObserverResult<TQueryData, TError>>

data class SessionState(val user: UserEntity?, val refetch: RefetchFunction<UserEntity, Error>)

val SessionContext = createContext<SessionState>()

val SessionManager = FC<PropsWithChildren> { props ->
	val navigate = useNavigate()
	val query = useMeQuery()
	val (sessionState, setSessionState) = useState(SessionState(null, query.refetch))
	query.refetch

	useEffect(query.isLoading, query.isError, query.data) {
		if (!query.isError) {
			setSessionState(SessionState(query.data, query.refetch))
		} else {
			setSessionState(SessionState(null, query.refetch))
			navigate("users/login", jso { replace = true })
		}
	}
	SessionContext.Provider(sessionState) {
		+props.children
	}
}
