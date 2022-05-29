package entities

@kotlinx.serialization.Serializable
sealed class SearchResult {
	abstract val id: Int
	abstract val overview: String
	abstract val posterPath: String?
	abstract val releaseDate: String
	abstract val title: String

	@kotlinx.serialization.Serializable
	data class TVResult(
		override val id: Int,
		override val overview: String,
		override val posterPath: String?,
		override val releaseDate: String,
		override val title: String,
	) : SearchResult()

	@kotlinx.serialization.Serializable
	data class MovieResult(
		override val id: Int,
		override val overview: String,
		override val posterPath: String?,
		override val releaseDate: String,
		override val title: String,
	) : SearchResult()
}
