package entities

import lib.PosterPath

@kotlinx.serialization.Serializable
sealed class SearchResultEntity {
	abstract val id: Int
	abstract val overview: String
	abstract val posterPath: PosterPath
	abstract val releaseDate: String?
	abstract val title: String
	abstract val request: RequestData?

	@kotlinx.serialization.Serializable
	data class RequestData(val id: Int, val status: RequestStatus)

	@kotlinx.serialization.Serializable
	data class TVResult(
		override val id: Int,
		override val overview: String,
		override val posterPath: PosterPath,
		override val releaseDate: String?,
		override val title: String,
		override val request: RequestData? = null
	) : SearchResultEntity()

	@kotlinx.serialization.Serializable
	data class MovieResult(
		override val id: Int,
		override val overview: String,
		override val posterPath: PosterPath,
		override val releaseDate: String?,
		override val title: String,
		override val request: RequestData? = null
	) : SearchResultEntity()
}

fun SearchResultEntity.toMediaType() = when (this) {
	is SearchResultEntity.TVResult -> MediaType.TV
	is SearchResultEntity.MovieResult -> MediaType.MOVIE
}
