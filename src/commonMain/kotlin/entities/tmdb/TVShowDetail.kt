package entities.tmdb

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TVShowDetail(
	@SerialName("backdrop_path") override val backdropPath: String?,
	@SerialName("created_by") val createdBy: List<CreatedBy> = emptyList(),
	@SerialName("episode_run_time") val episodeRuntime: List<Int> = emptyList(),
	@SerialName("first_air_date") override val firstAirDate: String?,
	@SerialName("in_production") val inProduction: Boolean,
	@SerialName("last_air_date") val lastAirDate: String,
	@SerialName("last_episode_to_air") val lastEpisodeToAir: Episode? = null,
	@SerialName("name") override val title: String,
	@SerialName("next_episode_to_air") val nextEpisodeToAir: Episode? = null,
	@SerialName("number_of_episodes") val numberOfEpisodes: Int,
	@SerialName("number_of_seasons") val numberOfSeasons: Int,
	@SerialName("origin_country") override val originCountry: List<String>,
	@SerialName("original_language") override val originalLanguage: String,
	@SerialName("original_name") override val originalTitle: String,
	@SerialName("poster_path") override val posterPath: String?,
	@SerialName("production_companies") val productionCompany: List<ProductionCompany> = emptyList(),
	@SerialName("production_countries") val productionCountries: List<ProductionCountry> = emptyList(),
	@SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguage> = emptyList(),
	@SerialName("vote_average") override val voteAverage: Float,
	@SerialName("vote_count") override val voteCount: Int,
	override val id: Int,
	override val overview: String?,
	override val popularity: Float,
	val genres: List<Genre> = emptyList(),
	val homepage: String,
	val languages: List<String> = emptyList(),
	val networks: List<Network> = emptyList(),
	val seasons: List<Season> = emptyList(),
	val status: String,
	val tagline: String,
	val type: String,
) : BaseTVShowEntity

@kotlinx.serialization.Serializable
data class CreatedBy(
	@SerialName("credit_id") val creditId: String,
	@SerialName("profile_path") val profilePath: String?,
	val gender: Int,
	val id: Int,
	val name: String,
)

@kotlinx.serialization.Serializable
data class Episode(
	@SerialName("air_date") val airDate: String,
	@SerialName("episode_number") val episodeNumber: Int,
	@SerialName("production_code") val productionCode: String,
	@SerialName("season_number") val seasonNumber: Int,
	@SerialName("still_path") val stillPath: String?,
	@SerialName("vote_average") val voteAverage: Float,
	@SerialName("vote_count") val voteCount: Int,
	val id: Int,
	val name: String,
	val overview: String,
)

@kotlinx.serialization.Serializable
data class Network(
	@SerialName("logo_path") val logoPath: String?,
	@SerialName("origin_country") val originCountry: String,
	val id: Int,
	val name: String,
)

@kotlinx.serialization.Serializable
data class Season(
	@SerialName("air_date") val airDate: String,
	@SerialName("episode_count") val episodeCount: Int,
	val id: Int,
	val name: String,
	val overview: String,
	@SerialName("poster_path") val posterPath: String,
	@SerialName("season_number") val seasonNumber: Int,
)