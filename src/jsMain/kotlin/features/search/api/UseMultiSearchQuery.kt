package features.search.api

import entities.SearchResult
import http.SearchResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.createQueryKey
import lib.reactQuery.parseListQueryKey
import react.query.*


private interface MultiSearchQueryKey : QueryKey, List<String>
private typealias MultiSearchQueryResult = List<SearchResult>

private val searchMultiQuery: QueryFunction<MultiSearchQueryResult, MultiSearchQueryKey> = { context ->
	MainScope().promise {
		val queryKey = parseListQueryKey<String>(context.queryKey)
		val searchTerm = queryKey.last()

		val result = apiClient.get(SearchResource.Multi(searchTerm = searchTerm))

		return@promise result.body()
	}
}

const val MultiSearchQueryKeyPrefix = "multi-search"
fun useMultiSearchQuery(searchTerm: String): UseQueryResult<MultiSearchQueryResult, Error> {
	val queryKey = createQueryKey<MultiSearchQueryKey>(MultiSearchQueryKeyPrefix, searchTerm.trim())

	val options: UseQueryOptions<MultiSearchQueryResult, Error, MultiSearchQueryResult, MultiSearchQueryKey> = jso {
		enabled = searchTerm.isNotBlank()
	}

	return useQuery(queryKey, searchMultiQuery, options)
}

