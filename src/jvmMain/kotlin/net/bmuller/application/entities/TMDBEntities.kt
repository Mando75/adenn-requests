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

	@kotlinx.serialization.Serializable
	data class MovieResult(
		@SerialName("poster_path") val posterPath: String?,
		val adult: Boolean,
		val overview: String,
		@SerialName("release_date") val releaseDate: String,
		@SerialName("original_title") val originalTitle: String,
		@SerialName("genre_ids") val genreIds: List<Int>,
		val id: Int,
		@SerialName("media_type") override val mediaType: MediaType,
		@SerialName("original_language") val originalLanguage: String,
		@SerialName("backdrop_path") val backdropPath: String?,
		val title: String,
		val popularity: Float,
		@SerialName("vote_count") val voteCount: Int,
		val video: Boolean,
		@SerialName("vote_average") val voteAverage: Float,
	) : MultiSearchEntity()

	@kotlinx.serialization.Serializable
	data class TVResult(
		@SerialName("poster_path") val poster_path: String?,
		val popularity: Float,
		val id: Int,
		val overview: String,
		@SerialName("backdrop_path") val backdropPath: String?,
		@SerialName("vote_average") val voteAverage: Float,
		@SerialName("media_type") override val mediaType: MediaType,
		@SerialName("first_air_date") val firstAirDate: String,
		@SerialName("origin_country") val originCountry: List<String>,
		@SerialName("genre_ids") val genreIds: List<Int>,
		@SerialName("original_language") val originalLanguage: String,
		@SerialName("vote_count") val voteCount: Int,
		val name: String,
		@SerialName("original_name") val originalName: String,
	) : MultiSearchEntity()

	@kotlinx.serialization.Serializable
	data class PersonResult(
		@SerialName("profile_path") val profilePath: String?,
		val adult: Boolean,
		val id: Int,
		@SerialName("media_type") override val mediaType: MediaType,
		@SerialName("known_for") val knownFor: MultiSearchEntity,
		val name: String,
		val popularity: Float,
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

