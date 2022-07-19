package lib

import kotlin.jvm.JvmInline

@kotlinx.serialization.Serializable
@JvmInline
value class PosterPath(val value: String)
object ImageTools {
	private const val PLACEHOLDER_IMAGE_URL = "https://via.placeholder.com/300x450.png?text=Poster%20Not%20Found"

	fun tmdbPosterPath(path: String?): PosterPath =
		path?.let { PosterPath("https://image.tmdb.org/t/p/w300_and_h450_face$path") }
			?: PosterPath(PLACEHOLDER_IMAGE_URL)
}