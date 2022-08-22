package features.requests.api

import entities.RequestStatus
import entities.UpdateRequestStatus
import entities.UpdateRequestStatusResponse
import features.search.api.MultiSearchQueryKeyPrefix
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.queryClient
import org.w3c.dom.HTMLButtonElement
import react.query.*

data class UpdateRequestStatusMutationVariables(
	val requestId: Int,
	val status: RequestStatus,
	val rejectionReason: String? = null,
	val target: HTMLButtonElement
)

private val updateRequestStatusMutation: MutationFunction<UpdateRequestStatusResponse, UpdateRequestStatusMutationVariables> =
	{ params ->
		MainScope().promise {
			val result = apiClient.post(RequestResource.Id.Status(parent = RequestResource.Id(id = params.requestId))) {
				setBody(UpdateRequestStatus(status = params.status, rejectionReason = params.rejectionReason))
			}
			return@promise result.body()
		}
	}

fun useUpdateRequestStatusMutation(): UseMutationResult<UpdateRequestStatusResponse, Error, UpdateRequestStatusMutationVariables, *> {
	val options: UseMutationOptions<UpdateRequestStatusResponse, Error, UpdateRequestStatusMutationVariables, *> =
		jso {
			onSuccess = { _, params, _ ->
				queryClient.invalidateQueries<Any>(QueryKey<QueryKey>(MultiSearchQueryKeyPrefix))
				queryClient.invalidateQueries<Any>(QueryKey<QueryKey>(RequestsQueryKeyPrefix)).finally {
					params.target.blur()
				}
			}
		}

	return useMutation(updateRequestStatusMutation, options)
}