package db.model

import db.util.postgresEnumeration
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

@Suppress("unused")
object MediaItemTable : IntIdTable("media_items") {
	enum class MediaItemType {
		MOVIE,
		TV
	}


	val mediaType = postgresEnumeration<MediaItemType>("media_type", "Media_Type_Enum")
	val overview = text("overview").default("")
	val releaseDate = date("release_date").nullable()
	val title = text("title")
	val posterUrl = text("poster_url").nullable()
	val backgroundUrl = text("background_url").nullable()
	val tmdbUrl = text("tmdb_url")
	val popularity = float("popularity")
	val tmdbId = integer("tmdb_id")

	init {
		index(true, mediaType, tmdbId)
	}
}