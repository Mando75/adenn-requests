package api.mutations

import entities.RequestEntity
import entities.SearchResult
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import react.query.MutationFunction
import react.query.UseMutationOptions
import react.query.UseMutationResult
import react.query.useMutation
import support.apiClient

private val scope = MainScope()

private val submitRequestMutation: MutationFunction<RequestEntity, SearchResult> = { searchResult ->
	scope.promise {
		val result = apiClient.post(RequestResource()) {
			setBody(searchResult)
		}
		return@promise result.body()
	}
}

fun useSubmitRequestMutation(): UseMutationResult<RequestEntity, Error, SearchResult, *> {
	val options: UseMutationOptions<RequestEntity, Error, SearchResult, *> = jso {}

	return useMutation(submitRequestMutation, options)
}