package entities.tmdb


interface BaseSearchResult {
	val page: Int

	val totalResults: Int

	val totalPages: Int
}

sealed interface BaseTMDBEntity {
	val backdropPath: String?
	val posterPath: String?
	val id: Int
	val title: String
	val overview: String?
	val popularity: Float
	val originalLanguage: String
	val originalTitle: String
	val voteAverage: Float
	val voteCount: Int
}

sealed interface DetailEntity
interface BaseMovieEntity : BaseTMDBEntity, DetailEntity {
	val releaseDate: String?
	val adult: Boolean
}

interface BaseTVShowEntity : BaseTMDBEntity, DetailEntity {
	val firstAirDate: String?
	val originCountry: List<String>
}