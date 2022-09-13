package features.dashboard.api

import api.RequestsQueryResponse
import api.useRequestsQuery
import entities.RequestFilters
import entities.RequestStatus
import hooks.UsePagination
import react.query.UseQueryResult

fun useRecentlyAddedQuery(): Pair<UseQueryResult<RequestsQueryResponse, Error>, UsePagination> {
	val filters = RequestFilters(status = listOf(RequestStatus.FULFILLED))

	return useRequestsQuery(filters)
}