package features.dashboard.api

import api.RequestsQueryResponse
import api.useRequestsQuery
import entities.RequestFilters
import hooks.UsePagination
import providers.useSessionUser
import react.query.UseQueryResult

fun useMyRequestsQuery(): Pair<UseQueryResult<RequestsQueryResponse, Error>, UsePagination> {
	val me = useSessionUser()
	val requesterIds = me?.let { listOf(it.id) } ?: emptyList()
	val filters = RequestFilters(requesterIds = requesterIds)

	return useRequestsQuery(filters)
}