package lib


object ImageTools {
	private const val PLACEHOLDER_IMAGE_URL = "https://via.placeholder.com/300x450.png?text=Poster%20Not%20Found"

	fun tmdbPosterPath(path: String?): String =
		path?.let { "https://image.tmdb.org/t/p/w300_and_h450_face$path" }
			?: PLACEHOLDER_IMAGE_URL
}