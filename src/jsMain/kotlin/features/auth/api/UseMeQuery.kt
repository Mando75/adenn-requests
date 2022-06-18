package features.auth.api

import entities.UserEntity
import http.UserResource
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.ApiClient.apiClient
import lib.ReactQuery.createQueryKey
import react.query.*


private typealias MeQueryKey = QueryKey

private val meQuery: QueryFunction<UserEntity?, MeQueryKey> = {
	MainScope().promise {
		val result = apiClient.get(UserResource.Me()) { expectSuccess = false }
		return@promise when (result.status) {
			HttpStatusCode.OK -> result.body<UserEntity>()
			HttpStatusCode.Forbidden -> null
			HttpStatusCode.Unauthorized -> null
			else -> throw Error(result.body<String>())
		}
	}
}

fun useMeQuery(): UseQueryResult<UserEntity?, Error> {
	val queryKey = createQueryKey<MeQueryKey>("me-query")
	val options: UseQueryOptions<UserEntity?, Error, UserEntity?, MeQueryKey> = jso {
		retry = { _, _ -> false }
	}

	return useQuery(queryKey, meQuery, options)
}

