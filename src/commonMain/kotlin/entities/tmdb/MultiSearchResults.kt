package entities.tmdb

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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
		@SerialName("genre_ids") val genreIds: List<Int>,
		override val id: Int,
		@SerialName("media_type") override val mediaType: MediaType = MediaType.movie,
		@SerialName("original_language") override val originalLanguage: String,
		@SerialName("backdrop_path") override val backdropPath: String?,
		override val title: String,
		override val popularity: Float,
		@SerialName("vote_count") override val voteCount: Int,
		val video: Boolean,
		@SerialName("vote_average") override val voteAverage: Float,
	) : BaseMovieEntity, MultiSearchEntity()

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
		@SerialName("genre_ids") val genreIds: List<Int>,
		@SerialName("original_language") override val originalLanguage: String,
		@SerialName("vote_count") override val voteCount: Int,
		@SerialName("name") override val title: String,
		@SerialName("original_name") override val originalTitle: String,
	) : BaseTVShowEntity, MultiSearchEntity()

	@kotlinx.serialization.Serializable
	data class PersonResult(
		@SerialName("profile_path") val profilePath: String?,
		val adult: Boolean,
		override val id: Int,
		@SerialName("media_type") override val mediaType: MediaType = MediaType.person,
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