package db.tables

import db.util.postgresEnumeration
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

@Suppress("unused")
object RequestTable : IntIdTable("requests") {
	enum class RequestStatus {
		REQUESTED,
		FULFILLED,
		REJECTED,
		WAITING,
		IMPORTED,
		DOWNLOADING
	}

	enum class MediaType {
		MOVIE,
		TV
	}

	val tmdbId: Column<Int> = integer("tmdb_id")
	val mediaType: Column<MediaType> = postgresEnumeration("media_type", "Media_Type_Enum")
	val title: Column<String> = text("title")
	val posterPath: Column<String> = text("poster_path")
	val releaseDate: Column<Instant?> = timestamp("release_date").nullable()


	val status: Column<RequestStatus> = postgresEnumeration<RequestStatus>("status", "Request_Status_Enum").index()
	val requesterId: Column<Int> = integer("requester_id").references(UserTable.id).index("requester_id_fk")
	val rejectionReason: Column<String?> = text("rejection_reason").nullable()

	// Dates Columns
	val dateRequested: Column<Instant> = timestamp("date_requested").default(Instant.now())
	val dateFulfilled: Column<Instant?> = timestamp("date_fulfilled").nullable()
	val dateRejected: Column<Instant?> = timestamp("date_rejected").nullable()
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())


	init {
		uniqueIndex("unique_tmdb_id_for_media_type", tmdbId, mediaType)
	}
}