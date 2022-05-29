package net.bmuller.application.entities

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

interface BaseSearchResult {
	val page: Int

	val totalResults: Int

	val totalPages: Int
}

interface BaseMovieResult {
	val backdropPath: String?
	val genreIds: List<Int>
	val originalLanguage: String
	val originalTitle: String
	val posterPath: String?
	val releaseDate: String?
	val voteAverage: Float
	val voteCount: Int
	val adult: Boolean
	val id: Int
	val overview: String
	val popularity: Float
	val title: String
}

@kotlinx.serialization.Serializable
data class MovieResult(
	@SerialName("backdrop_path") override val backdropPath: String?,
	@SerialName("genre_ids") override val genreIds: List<Int>,
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
	val results: List<MovieResult>,
) : BaseSearchResult


interface BaseTVShowResult {
	val backdropPath: String?
	val firstAirDate: String?
	val genreIds: List<Int>
	val originCountry: List<String>
	val originalLanguage: String
	val originalName: String
	val posterPath: String?
	val voteAverage: Float
	val voteCount: Int
	val id: Int
	val title: String
	val overview: String
	val popularity: Float
}

@kotlinx.serialization.Serializable
data class TVShowResult(
	@SerialName("backdrop_path") override val backdropPath: String? = null,
	@SerialName("first_air_date") override val firstAirDate: String? = null,
	@SerialName("genre_ids") override val genreIds: List<Int>,
	@SerialName("origin_country") override val originCountry: List<String>,
	@SerialName("original_language") override val originalLanguage: String,
	@SerialName("original_name") override val originalName: String,
	@SerialName("poster_path") override val posterPath: String?,
	@SerialName("vote_average") override val voteAverage: Float,
	@SerialName("vote_count") override val voteCount: Int,
	override val id: Int,
	@SerialName("name") override val title: String,
	override val overview: String,
	override val popularity: Float,
) : BaseTVShowResult

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

@kotlinx.serialization.Serializable
data class MultiSearchResults(
	@SerialName("total_pages") override val totalPages: Int,
	@SerialName("total_results") override val totalResults: Int,
	override val page: Int,
	val results: List<MultiSearchEntity>
) : BaseSearchResult

@kotlinx.serialization.Serializable(with = MultiSearchEntity.MultiSearchEntitySerializer::class)
sealed class MultiSearchEntity {
	enum class MediaType {
		movie,
		tv,
		person
	}

	abstract val mediaType: MediaType
	abstract val title: String
	abstract val popularity: Float
	abstract val id: Int

	@kotlinx.serialization.Serializable
	data class MovieResult(
		@SerialName("poster_path") override val posterPath: String? = null,
		override val adult: Boolean,
		override val overview: String,
		@SerialName("release_date") override val releaseDate: String? = null,
		@SerialName("original_title") override val originalTitle: String,
		@SerialName("genre_ids") override val genreIds: List<Int>,
		override val id: Int,
		@SerialName("media_type") override val mediaType: MediaType = MediaType.movie,
		@SerialName("original_language") override val originalLanguage: String,
		@SerialName("backdrop_path") override val backdropPath: String?,
		override val title: String,
		override val popularity: Float,
		@SerialName("vote_count") override val voteCount: Int,
		val video: Boolean,
		@SerialName("vote_average") override val voteAverage: Float,
	) : BaseMovieResult, MultiSearchEntity()

	@kotlinx.serialization.Serializable
	data class TVResult(
		@SerialName("poster_path") override val posterPath: String?,
		override val popularity: Float,
		override val id: Int,
		override val overview: String,
		@SerialName("backdrop_path") override val backdropPath: String?,
		@SerialName("vote_average") override val voteAverage: Float,
		@SerialName("media_type") override val mediaType: MediaType = MediaType.tv,
		@SerialName("first_air_date") override val firstAirDate: String? = null,
		@SerialName("origin_country") override val originCountry: List<String>,
		@SerialName("genre_ids") override val genreIds: List<Int>,
		@SerialName("original_language") override val originalLanguage: String,
		@SerialName("vote_count") override val voteCount: Int,
		@SerialName("name") override val title: String,
		@SerialName("original_name") override val originalName: String,
	) : BaseTVShowResult, MultiSearchEntity()

	@kotlinx.serialization.Serializable
	data class PersonResult(
		@SerialName("profile_path") val profilePath: String?,
		val adult: Boolean,
		override val id: Int,
		@SerialName("media_type") override val mediaType: MediaType = MediaType.person,
		@SerialName("known_for") val knownFor: MultiSearchEntity,
		@SerialName("name") override val title: String,
		override val popularity: Float,
	) : MultiSearchEntity()

	object MultiSearchEntitySerializer : JsonContentPolymorphicSerializer<MultiSearchEntity>(MultiSearchEntity::class) {
		override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out MultiSearchEntity> {
			return when (val mediaType = element.jsonObject["media_type"]?.jsonPrimitive?.content) {
				MediaType.person.toString() -> PersonResult.serializer()
				MediaType.movie.toString() -> MovieResult.serializer()
				MediaType.tv.toString() -> TVResult.serializer()
				else -> throw Exception("Unknown Search Result: key 'media_type' was not found or does not match any known media type: $mediaType")
			}
		}
	}
}

