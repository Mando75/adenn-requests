package net.bmuller.application.http.tmdb

interface BaseSearchResult {
	val page: Int
	val total_results: Int
	val total_pages: Int
}

@kotlinx.serialization.Serializable
data class MovieResult(
	val poster_path: String?,
	val adult: Boolean,
	val overview: String,
	val release_date: String,
	val genre_ids: List<Int>,
	val id: Int,
	val original_title: String,
	val original_language: String,
	val title: String,
	val backdrop_path: String?,
	val popularity: Float,
	val vote_count: Int,
	val vote_average: Float
)

@kotlinx.serialization.Serializable
data class MovieSearchResults(
	override val page: Int,
	val results: List<MovieResult>,
	override val total_results: Int,
	override val total_pages: Int
) : BaseSearchResult

@kotlinx.serialization.Serializable
data class TVShowResult(
	val poster_path: String?,
	val popularity: Float,
	val id: Int,
	val backdrop_path: String?,
	val vote_average: Float,
	val overview: String,
	val first_air_date: String,
	val origin_country: List<String>,
	val genre_ids: List<Int>,
	val original_language: String,
	val vote_count: Int,
	val name: String,
	val original_name: String
)

@kotlinx.serialization.Serializable
data class TVShowSearchResults(
	override val page: Int,
	override val total_results: Int,
	override val total_pages: Int,
	val results: List<TVShowResult>
) : BaseSearchResult

@kotlinx.serialization.Serializable
data class TMDBConfigurationResult(
	val images: TMDBImageConfiguration,
	val change_keys: List<String>
)

@kotlinx.serialization.Serializable
data class TMDBImageConfiguration(
	val base_url: String,
	val secure_base_url: String,
	val backdrop_sizes: List<String>,
	val logo_sizes: List<String>,
	val poster_sizes: List<String>,
	val profile_sizes: List<String>,
	val still_sizes: List<String>
)