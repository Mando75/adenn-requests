package entities.tmdb

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MovieSearchResult(
	@SerialName("backdrop_path") override val backdropPath: String?,
	@SerialName("genre_ids") val genreIds: List<Int>,
	@SerialName("original_language") override val originalLanguage: String,
	@SerialName("original_title") override val originalTitle: String,
	@SerialName("poster_path") override val posterPath: String?,
	@SerialName("release_date") override val releaseDate: String?,
	@SerialName("vote_average") override val voteAverage: Float,
	@SerialName("vote_count") override val voteCount: Int,
	override val adult: Boolean,
	override val id: Int,
	override val overview: String,
	override val popularity: Float,
	override val title: String,
) : BaseMovieResult

@kotlinx.serialization.Serializable
data class MovieSearchResults(
	@SerialName("total_pages") override val totalPages: Int,
	@SerialName("total_results") override val totalResults: Int,
	override val page: Int,
	val results: List<MovieSearchResult>,
) : BaseSearchResult