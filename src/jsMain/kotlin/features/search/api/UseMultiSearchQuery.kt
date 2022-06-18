package features.search.api

import entities.SearchResult
import http.SearchResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.ApiClient.apiClient
import lib.ReactQuery.createQueryKey
import lib.ReactQuery.parseQueryKey
import react.query.*


private interface MultiSearchQueryKey : QueryKey, List<String>
private typealias MultiSearchQueryResult = List<SearchResult>

private val searchMultiQuery: QueryFunction<MultiSearchQueryResult, MultiSearchQueryKey> = { context ->
	MainScope().promise {
		val queryKey = parseQueryKey<String>(context.queryKey)
		val searchTerm = queryKey.last()

		val result = apiClient.get(SearchResource.Multi(searchTerm = searchTerm))

		return@promise result.body()
	}
}

fun useMultiSearchQuery(searchTerm: String): UseQueryResult<MultiSearchQueryResult, Error> {
	val queryKey = createQueryKey<MultiSearchQueryKey>("multi-search", searchTerm.trim())

	val options: UseQueryOptions<MultiSearchQueryResult, Error, MultiSearchQueryResult, MultiSearchQueryKey> = jso {
		enabled = searchTerm.isNotBlank()
	}

	return useQuery(queryKey, searchMultiQuery, options)
}

