package features.search.api

import entities.MediaType
import entities.Provider
import http.MediaResource
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlinx.js.jso
import lib.apiClient.apiClient
import lib.reactQuery.createQueryKey
import lib.reactQuery.parseTripleQueryKey
import react.query.*
import utils.IJsTriple

private interface MediaProvidersQueryKey : QueryKey, IJsTriple<String, Int, MediaType>

private typealias MediaProvidersQueryResponse = List<Provider>

private val mediaProvidersQuery: QueryFunction<MediaProvidersQueryResponse, MediaProvidersQueryKey> = { context ->
	MainScope().promise {
		val (_, id, mediaType) = parseTripleQueryKey<String, Int, MediaType>(context.queryKey)

		val result = apiClient.get(MediaResource.Providers(MediaResource(id = id, type = mediaType)))

		return@promise result.body()
	}
}

const val MediaProvidersQueryKeyPrefix = "media-providers"

fun useMediaProvidersQuery(id: Int?, mediaType: MediaType?): UseQueryResult<MediaProvidersQueryResponse, Error> {
	val queryKey =
		createQueryKey<MediaProvidersQueryKey>(MediaProvidersQueryKeyPrefix, id ?: 0, mediaType ?: MediaType.MOVIE)

	val options: UseQueryOptions<MediaProvidersQueryResponse, Error, MediaProvidersQueryResponse, MediaProvidersQueryKey> =
		jso {
			enabled = id != null && mediaType != null
		}

	return useQuery(queryKey, mediaProvidersQuery, options)
}