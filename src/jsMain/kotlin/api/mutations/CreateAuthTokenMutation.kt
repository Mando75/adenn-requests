package api.mutations

import entities.AuthTokenResponse
import http.AuthResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import react.query.MutationFunction
import react.query.UseMutationOptions
import react.query.UseMutationResult
import react.query.useMutation
import support.apiClient

private val scope = MainScope()

private val createAuthTokenMutation: MutationFunction<AuthTokenResponse, CreateAuthTokenVariables> = {
	scope.promise {
		val result = apiClient.post(AuthResource.Token())
		return@promise result.body()
	}

}

class CreateAuthTokenVariables

fun useCreateAuthTokenMutation(): UseMutationResult<AuthTokenResponse, Error, CreateAuthTokenVariables, *> {
	val options: UseMutationOptions<AuthTokenResponse, Error, CreateAuthTokenVariables, *> = jso {}

	return useMutation(createAuthTokenMutation, options)
}