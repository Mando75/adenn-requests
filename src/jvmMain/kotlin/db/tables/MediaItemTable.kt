package db.tables

import db.util.postgresEnumeration
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.LocalDate

@Suppress("unused")
object MediaItemTable : IntIdTable("media_items") {
	enum class MediaItemType {
		MOVIE,
		TV
	}


	val mediaType: Column<MediaItemType> = postgresEnumeration("media_type", "Media_Type_Enum")
	val overview: Column<String> = text("overview").default("")
	val releaseDate: Column<LocalDate?> = date("release_date").nullable()
	val title: Column<String> = text("title")
	val posterUrl: Column<String?> = text("poster_url").nullable()
	val backgroundUrl: Column<String?> = text("background_url").nullable()
	val tmdbUrl: Column<String> = text("tmdb_url")
	val popularity: Column<Float> = float("popularity")
	val tmdbId: Column<Int> = integer("tmdb_id")
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())


	init {
		index(true, mediaType, tmdbId)
	}
}