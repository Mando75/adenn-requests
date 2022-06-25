package features.requests.api

import entities.*
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.createQueryKey
import lib.reactQuery.parseQueryKey
import react.query.*

private interface RequestsQueryKey : QueryKey, List<String>
private typealias RequestsQueryResponse = PaginatedResponse<RequestEntity>

private val requestsQuery: QueryFunction<RequestsQueryResponse, RequestsQueryKey> = { context ->
	MainScope().promise {
		val queryKey = parseQueryKey<String>(context.queryKey)
		val pagination = Pagination(limit = 1, offset = 0)
		val filters = RequestFilters(status = listOf(RequestStatus.FULFILLED))

		val result = apiClient.get(RequestResource(filters = filters, pagination = pagination))

		return@promise result.body()
	}
}

fun useRequestsQuery(): UseQueryResult<RequestsQueryResponse, Error> {
	val queryKey = createQueryKey<RequestsQueryKey>("requests-query")

	val options: UseQueryOptions<RequestsQueryResponse, Error, RequestsQueryResponse, RequestsQueryKey> = jso {}

	return useQuery(queryKey, requestsQuery, options)
}
