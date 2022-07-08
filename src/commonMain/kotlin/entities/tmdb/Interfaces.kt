package entities.tmdb

interface BaseSearchResult {
	val page: Int

	val totalResults: Int

	val totalPages: Int
}

interface BaseMovieResult {
	val backdropPath: String?
	val originalLanguage: String
	val originalTitle: String
	val posterPath: String?
	val releaseDate: String?
	val voteAverage: Float
	val voteCount: Int
	val adult: Boolean
	val id: Int
	val overview: String?
	val popularity: Float
	val title: String
}

interface BaseTVShowResult {
	val backdropPath: String?
	val firstAirDate: String?
	val originCountry: List<String>
	val originalLanguage: String
	val originalName: String
	val posterPath: String?
	val voteAverage: Float
	val voteCount: Int
	val id: Int
	val title: String
	val overview: String?
	val popularity: Float
}