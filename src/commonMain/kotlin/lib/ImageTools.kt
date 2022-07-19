package lib

import kotlin.jvm.JvmInline

object ImageTools {
	private const val PLACEHOLDER_POSTER_URL = "https://via.placeholder.com/300x450.png?text=Poster%20Not%20Found"

	fun tmdbPosterPath(path: String?): PosterPath =
		path?.let { PosterPath("https://image.tmdb.org/t/p/w300_and_h450_face$path") }
			?: PosterPath(PLACEHOLDER_POSTER_URL)

	fun tmdbBackdropPath(path: String): BackdropPath =
		BackdropPath("https://image.tmdb.org/t/p/w1920_and_h800_multi_faces$path")
}

@kotlinx.serialization.Serializable
@JvmInline
value class PosterPath(val value: String)

@kotlinx.serialization.Serializable
@JvmInline
value class BackdropPath(val value: String)
