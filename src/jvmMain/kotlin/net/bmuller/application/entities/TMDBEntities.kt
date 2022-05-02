package net.bmuller.application.entities

import kotlinx.serialization.SerialName

interface BaseSearchResult {
	val page: Int

	val totalResults: Int

	val totalPages: Int
}

@kotlinx.serialization.Serializable
data class MovieResult(
	@SerialName("backdrop_path") val backdropPath: String?,
	@SerialName("genre_ids") val genreIds: List<Int>,
	@SerialName("original_language") val originalLanguage: String,
	@SerialName("original_title") val originalTitle: String,
	@SerialName("poster_path") val posterPath: String?,
	@SerialName("release_date") val releaseDate: String,
	@SerialName("vote_average") val voteAverage: Float,
	@SerialName("vote_count") val voteCount: Int,
	val adult: Boolean,
	val id: Int,
	val overview: String,
	val popularity: Float,
	val title: String,
)

@kotlinx.serialization.Serializable
data class MovieSearchResults(
	@SerialName("total_pages") override val totalPages: Int,
	@SerialName("total_results") override val totalResults: Int,
	override val page: Int,
	val results: List<MovieResult>,
) : BaseSearchResult

@kotlinx.serialization.Serializable
data class TVShowResult(
	@SerialName("backdrop_path") val backdropPath: String?,
	@SerialName("first_air_date") val firstAirDate: String,
	@SerialName("genre_ids") val genreIds: List<Int>,
	@SerialName("origin_country") val originCountry: List<String>,
	@SerialName("original_language") val originalLanguage: String,
	@SerialName("original_name") val originalName: String,
	@SerialName("poster_path") val poster_path: String?,
	@SerialName("vote_average") val voteAverage: Float,
	@SerialName("vote_count") val voteCount: Int,
	val id: Int,
	val name: String,
	val overview: String,
	val popularity: Float,
)

@kotlinx.serialization.Serializable
data class TVShowSearchResults(
	@SerialName("total_pages") override val totalPages: Int,
	@SerialName("total_results") override val totalResults: Int,
	override val page: Int,
	val results: List<TVShowResult>
) : BaseSearchResult

@kotlinx.serialization.Serializable
data class TMDBConfigurationResult(
	val images: TMDBImageConfiguration,
	@SerialName("change_keys") val changeKeys: List<String>
)

@kotlinx.serialization.Serializable
data class TMDBImageConfiguration(
	@SerialName("base_url") val baseUrl: String,
	@SerialName("secure_base_url") val secureBaseUrl: String,
	@SerialName("backdrop_sizes") val backdropSizes: List<String>,
	@SerialName("logo_sizes") val logoSizes: List<String>,
	@SerialName("poster_sizes") val posterSizes: List<String>,
	@SerialName("profile_sizes") val profileSizes: List<String>,
	@SerialName("still_sizes") val stillSizes: List<String>
)