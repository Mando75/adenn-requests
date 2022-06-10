package support

import kotlinx.js.jso
import react.query.DefaultOptions
import react.query.QueryClient
import react.query.QueryObserverOptions


private val queryClientOptions: QueryObserverOptions<*, Error, *, *, *> = jso {
	refetchOnWindowFocus = false
	staleTime = 1000 * 60 * 5 // 5 minutes
	cacheTime = 1000 * 60 * 15 // 15 minutes
}

private val defaultQueryOptions: DefaultOptions<Error> = jso {
	queries = queryClientOptions
}

val queryClient = QueryClient(jso { defaultOptions = defaultQueryOptions })