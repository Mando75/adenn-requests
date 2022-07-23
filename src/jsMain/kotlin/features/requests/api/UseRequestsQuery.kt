package features.requests.api

import entities.PaginatedResponse
import entities.RequestFilters
import entities.RequestListItem
import hooks.UsePagination
import hooks.usePagination
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.createQueryKey
import lib.reactQuery.parseTripleQueryKey
import react.query.*
import utils.IJsTriple

private interface RequestsQueryKey : QueryKey, IJsTriple<String, Long, RequestFilters>
private typealias RequestsQueryResponse = PaginatedResponse<RequestListItem>

private val requestsQuery: QueryFunction<RequestsQueryResponse, RequestsQueryKey> = { context ->
	MainScope().promise {
		val (_, page, filters) = parseTripleQueryKey<String, Long, RequestFilters>(context.queryKey)

		val result = apiClient.get(
			RequestResource(
				searchTerm = filters.searchTerm,
				mediaType = filters.mediaType,
				status = filters.status,
				page = page
			)
		)

		return@promise result.body()
	}
}

const val RequestsQueryKeyPrefix = "requests-query"

fun useRequestsQuery(filters: RequestFilters): Pair<UseQueryResult<RequestsQueryResponse, Error>, UsePagination> {
	val pagination = usePagination()
	val queryKey = createQueryKey<RequestsQueryKey>(RequestsQueryKeyPrefix, pagination.queryPage, filters)

	val options: UseQueryOptions<RequestsQueryResponse, Error, RequestsQueryResponse, RequestsQueryKey> = jso {
		keepPreviousData = true
	}

	val query = useQuery(queryKey, requestsQuery, options)

	return Pair(query, pagination)
}
