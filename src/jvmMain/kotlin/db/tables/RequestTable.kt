package db.tables

import db.util.postgresEnumeration
import entities.RequestEntity
import entities.RequestStatus
import entities.UserEntity
import lib.parseDateColumn
import lib.parseNullableDateColumn
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

@Suppress("unused")
object RequestTable : IntIdTable("requests") {

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
	val dateFulfilled: Column<Instant?> = timestamp("date_fulfilled").nullable()
	val dateRejected: Column<Instant?> = timestamp("date_rejected").nullable()
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())


	init {
		uniqueIndex("unique_tmdb_id_for_media_type", tmdbId, mediaType)
	}
}

fun ResultRow.toRequestEntity(requester: UserEntity? = null): RequestEntity {
	return when (this[RequestTable.mediaType]) {
		RequestTable.MediaType.MOVIE -> RequestEntity.MovieRequest(
			id = get(RequestTable.id).value,
			tmdbId = get(RequestTable.tmdbId),
			title = get(RequestTable.title),
			posterPath = get(RequestTable.posterPath),
			releaseDate = parseNullableDateColumn(get(RequestTable.releaseDate)?.epochSecond),
			status = get(RequestTable.status),
			requester = requester,
			rejectionReason = get(RequestTable.rejectionReason),
			dateFulfilled = parseNullableDateColumn(get(RequestTable.dateFulfilled)?.epochSecond),
			dateRejected = parseNullableDateColumn(get(RequestTable.dateRejected)?.epochSecond),
			createdAt = parseDateColumn(get(RequestTable.createdAt).epochSecond),
			modifiedAt = parseDateColumn(get(RequestTable.modifiedAt).epochSecond)
		)
		RequestTable.MediaType.TV -> RequestEntity.TVShowRequest(
			id = get(RequestTable.id).value,
			tmdbId = get(RequestTable.tmdbId),
			title = get(RequestTable.title),
			posterPath = get(RequestTable.posterPath),
			releaseDate = parseNullableDateColumn(get(RequestTable.releaseDate)?.epochSecond),
			status = get(RequestTable.status),
			requester = requester,
			rejectionReason = get(RequestTable.rejectionReason),
			dateFulfilled = parseNullableDateColumn(get(RequestTable.dateFulfilled)?.epochSecond),
			dateRejected = parseNullableDateColumn(get(RequestTable.dateRejected)?.epochSecond),
			createdAt = parseDateColumn(get(RequestTable.createdAt).epochSecond),
			modifiedAt = parseDateColumn(get(RequestTable.modifiedAt).epochSecond)
		)
	}
}