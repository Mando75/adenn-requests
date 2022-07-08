package entities.tmdb

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class Genre(val id: Int, val name: String)

@kotlinx.serialization.Serializable
data class ProductionCompany(
	@SerialName("logo_path") val logoPath: String?,
	@SerialName("origin_country") val originCountry: String,
	val id: Int,
	val name: String,
)

@kotlinx.serialization.Serializable
data class ProductionCountry(
	@SerialName("iso_3166_1") val iso31661: String,
	val name: String
)

@kotlinx.serialization.Serializable
data class SpokenLanguage(
	@SerialName("english_name") val englishName: String?,
	@SerialName("iso_639_1") val iso6391: String,
	val name: String,
)

