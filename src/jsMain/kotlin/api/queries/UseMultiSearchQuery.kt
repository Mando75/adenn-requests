package api.queries

import entities.SearchResult
import http.SearchResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import react.query.*
import support.CreateQueryKey
import support.ParseQueryKey
import support.apiClient

private val scope = MainScope()

private interface MultiSearchQueryKey : QueryKey, List<String>
private typealias MultiSearchQueryResult = List<SearchResult>

private val searchMultiQuery: QueryFunction<MultiSearchQueryResult, MultiSearchQueryKey> = { context ->
	scope.promise {
		val queryKey = ParseQueryKey<String>(context.queryKey)
		console.log(queryKey)
		val searchTerm = queryKey.last()

		val result = apiClient.get(SearchResource.Multi(searchTerm = searchTerm))

		return@promise result.body()
	}
}

fun useMultiSearchQuery(searchTerm: String): UseQueryResult<MultiSearchQueryResult, Error> {
	val queryKey = CreateQueryKey<MultiSearchQueryKey>("multi-search", searchTerm)

	val options: UseQueryOptions<MultiSearchQueryResult, Error, MultiSearchQueryResult, MultiSearchQueryKey> = jso {
		enabled = searchTerm.isNotBlank()
	}

	return useQuery(queryKey, searchMultiQuery, options)
}

