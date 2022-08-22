package features.requests.api

import entities.RequestStatus
import entities.UpdateRequestStatus
import entities.UpdateRequestStatusResponse
import http.RequestResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.queryClient
import react.query.*

data class UpdateRequestStatusMutationVariables(
	val requestId: Int,
	val status: RequestStatus,
	val rejectionReason: String? = null
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
			onSuccess = { _, _, _ ->
				queryClient.invalidateQueries<Any>(QueryKey<QueryKey>(RequestsQueryKeyPrefix))
			}
		}

	return useMutation(updateRequestStatusMutation, options)
}