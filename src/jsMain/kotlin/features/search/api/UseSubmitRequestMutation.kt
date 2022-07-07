package features.search.api

import entities.RequestEntity
import entities.SearchResult
import features.requests.api.RequestsQueryKeyPrefix
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import react.query.*


private val submitRequestMutation: MutationFunction<RequestEntity, SearchResult> = { searchResult ->
	MainScope().promise {
		val result = apiClient.post(RequestResource()) {
			setBody(searchResult)
		}
		return@promise result.body()
	}
}

fun useSubmitRequestMutation(): UseMutationResult<RequestEntity, Error, SearchResult, *> {
	val queryClient = useQueryClient()
	val options: UseMutationOptions<RequestEntity, Error, SearchResult, *> = jso {
		onSuccess = { _, _, _ ->
			queryClient.invalidateQueries<Any>(QueryKey<QueryKey>(RequestsQueryKeyPrefix))
		}
	}

	return useMutation(submitRequestMutation, options)
}