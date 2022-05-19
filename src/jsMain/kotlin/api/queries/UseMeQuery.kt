package api.queries

import entities.UserEntity
import http.UserResource
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import react.query.QueryKey
import react.query.UseQueryOptions
import react.query.UseQueryResult
import react.query.useQuery
import support.apiClient

private val scope = MainScope()
fun useMeQuery(): UseQueryResult<UserEntity?, Error> {
	val queryKey: QueryKey = "me-query".unsafeCast<QueryKey>()
	val options: UseQueryOptions<UserEntity?, Error, UserEntity?, QueryKey> = jso {
		retry = { _, _ -> false }
	}

	return useQuery(queryKey, { scope.promise { meQuery() } }, options)
}

suspend fun meQuery(): UserEntity? {
	val result = apiClient.get(UserResource.Me()) { expectSuccess = false }
	return when (result.status) {
		HttpStatusCode.OK -> result.body<UserEntity>()
		HttpStatusCode.Forbidden -> null
		HttpStatusCode.Unauthorized -> null
		else -> throw Error(result.body<String>())
	}
}

