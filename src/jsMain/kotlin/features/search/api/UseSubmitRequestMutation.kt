package features.search.api

import entities.CreatedRequest
import entities.RequestListItem
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


private val submitRequestMutation: MutationFunction<CreatedRequest, SearchResult> = { searchResult ->
	MainScope().promise {
		val result = apiClient.post(RequestResource()) {
			setBody(searchResult)
		}
		return@promise result.body()
	}
}

fun useSubmitRequestMutation(): UseMutationResult<RequestListItem, Error, SearchResult, *> {
	val queryClient = useQueryClient()
	val options: UseMutationOptions<RequestListItem, Error, SearchResult, *> = jso {
		onSuccess = { _, _, _ ->
			queryClient.invalidateQueries<Any>(QueryKey<QueryKey>(RequestsQueryKeyPrefix))
		}
	}

	return useMutation(submitRequestMutation, options)
}