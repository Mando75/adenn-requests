package api.queries

import entities.LoginUrlResponse
import http.AuthResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import react.query.QueryKey
import react.query.UseQueryResult
import react.query.useQuery
import support.apiClient

private val scope = MainScope()

fun usePlexLoginUrl(host: String): UseQueryResult<LoginUrlResponse, Error> {
	val queryKey: QueryKey = "login-url-query".unsafeCast<QueryKey>()

	return useQuery(queryKey, {
		scope.promise { apiClient.get(AuthResource.Plex.LoginUrl(forwardHost = host)).body<LoginUrlResponse>() }
	}, jso {
		enabled = false
	})
}