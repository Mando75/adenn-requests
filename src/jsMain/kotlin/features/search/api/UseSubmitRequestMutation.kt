package features.search.api

import entities.RequestEntity
import entities.SearchResult
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import react.query.MutationFunction
import react.query.UseMutationOptions
import react.query.UseMutationResult
import react.query.useMutation


private val submitRequestMutation: MutationFunction<RequestEntity, SearchResult> = { searchResult ->
	MainScope().promise {
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