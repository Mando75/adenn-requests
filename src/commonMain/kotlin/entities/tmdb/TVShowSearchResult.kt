package entities.tmdb

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TVShowSearchResult(
	@SerialName("backdrop_path") override val backdropPath: String? = null,
	@SerialName("first_air_date") override val firstAirDate: String? = null,
	@SerialName("genre_ids") val genreIds: List<Int>,
	@SerialName("origin_country") override val originCountry: List<String>,
	@SerialName("original_language") override val originalLanguage: String,
	@SerialName("original_name") override val originalTitle: String,
	@SerialName("poster_path") override val posterPath: String? = null,
	@SerialName("vote_average") override val voteAverage: Float,
	@SerialName("vote_count") override val voteCount: Int,
	override val id: Int,
	@SerialName("name") override val title: String,
	override val overview: String,
	override val popularity: Float,
) : BaseTVShowEntity

@kotlinx.serialization.Serializable
data class TVShowSearchResults(
	@SerialName("total_pages") override val totalPages: Int,
	@SerialName("total_results") override val totalResults: Int,
	override val page: Int,
	val results: List<TVShowSearchResult>
) : BaseSearchResult