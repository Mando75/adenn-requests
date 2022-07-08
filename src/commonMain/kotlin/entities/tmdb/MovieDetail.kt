package entities.tmdb

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@kotlinx.serialization.Serializable
data class MovieDetail(
	@SerialName("backdrop_path") override val backdropPath: String?,
	@SerialName("imdb_id") val imdbId: Int,
	@SerialName("original_language") override val originalLanguage: String,
	@SerialName("original_title") override val originalTitle: String,
	@SerialName("poster_path") override val posterPath: String?,
	@SerialName("production_companies") val productionCompanies: List<ProductionCompany>,
	@SerialName("production_countries") val productionCountries: List<ProductionCountry>,
	@SerialName("release_date") override val releaseDate: String?,
	@SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
	@SerialName("vote_average") override val voteAverage: Float,
	@SerialName("vote_count") override val voteCount: Int,
	override val adult: Boolean,
	override val id: Int,
	override val overview: String?,
	override val popularity: Float,
	override val title: String,
	val budget: Int,
	val genres: List<Genre>,
	val homepage: String?,
	val revenue: Int,
	val runtime: Int?,
	val status: MovieStatus,
	val tagline: String?,
	val video: Boolean,
) : BaseMovieResult


@kotlinx.serialization.Serializable
data class Genre(val id: Int, val name: String)

@kotlinx.serialization.Serializable
data class ProductionCompany(
	val name: String,
	val id: Int,
	@SerialName("logo_path") val logoPath: String,
	@SerialName("origin_country") val originCountry: String
)

@kotlinx.serialization.Serializable
data class ProductionCountry(
	@SerialName("iso_3166_1") val iso31661: String,
	val name: String
)

@kotlinx.serialization.Serializable
data class SpokenLanguage(@SerialName("iso_639_1") val iso6391: String, val name: String)

@kotlinx.serialization.Serializable(with = MovieStatusSerializer::class)
enum class MovieStatus(val key: String) {
	Rumored("Rumored"),
	Planned("Planned"),
	InProduction("In Production"),
	PostProduction("Post Production"),
	Released("Released"),
	Canceled("Canceled"),
	Unknown("Unknown");

	companion object {
		fun findByKey(key: String, default: MovieStatus = Unknown): MovieStatus {
			return MovieStatus.values().find { it.key === key } ?: default
		}
	}
}

object MovieStatusSerializer : KSerializer<MovieStatus> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MovieStatus", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: MovieStatus) {
		encoder.encodeString(value.key)
	}

	override fun deserialize(decoder: Decoder): MovieStatus {
		return try {
			val key = decoder.decodeString()
			MovieStatus.findByKey(key)
		} catch (e: IllegalArgumentException) {
			MovieStatus.Unknown
		}
	}
}