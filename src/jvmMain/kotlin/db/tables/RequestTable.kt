package db.tables

import db.util.postgresEnumeration
import entities.RequestStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object RequestTable : IntIdTable("requests") {

	enum class MediaType {
		MOVIE,
		TV
	}

	val tmdbId: Column<Int> = integer("tmdb_id")
	val title: Column<String> = text("title").index()
	val mediaType: Column<MediaType> = postgresEnumeration("media_type", "Media_Type_Enum")
	val status: Column<RequestStatus> =
		postgresEnumeration<RequestStatus>("status", "Request_Status_Enum").index().default(RequestStatus.REQUESTED)
	val requesterId: Column<Int> = integer("requester_id").references(UserTable.id).index()
	val rejectionReason: Column<String?> = text("rejection_reason").nullable()

	// Dates Columns
	val dateFulfilled: Column<Instant?> = timestamp("date_fulfilled").nullable()
	val dateRejected: Column<Instant?> = timestamp("date_rejected").nullable()
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())


	init {
		uniqueIndex(tmdbId, mediaType)
	}
}
