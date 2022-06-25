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
import kotlin.reflect.full.primaryConstructor

@Suppress("unused")
object RequestTable : IntIdTable("requests") {

	enum class MediaType {
		MOVIE,
		TV
	}

	val tmdbId: Column<Int> = integer("tmdb_id")
	val mediaType: Column<MediaType> = postgresEnumeration("media_type", "Media_Type_Enum")
	val title: Column<String> = text("title").index()
	val posterPath: Column<String> = text("poster_path")
	val releaseDate: Column<String?> = text("release_date").nullable()


	val status: Column<RequestStatus> = postgresEnumeration<RequestStatus>("status", "Request_Status_Enum").index()
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

fun ResultRow.toRequestEntity(requester: UserEntity? = null): RequestEntity {
	val requestKlass = when (get(RequestTable.mediaType)) {
		RequestTable.MediaType.MOVIE -> RequestEntity.MovieRequest::class
		RequestTable.MediaType.TV -> RequestEntity.TVShowRequest::class
	}
	return requestKlass.primaryConstructor!!.call(
		get(RequestTable.id).value,
		get(RequestTable.tmdbId),
		get(RequestTable.title),
		get(RequestTable.posterPath),
		get(RequestTable.releaseDate),
		get(RequestTable.status),
		requester,
		get(RequestTable.rejectionReason),
		parseNullableDateColumn(get(RequestTable.dateFulfilled)?.epochSecond),
		parseNullableDateColumn(get(RequestTable.dateRejected)?.epochSecond),
		parseDateColumn(get(RequestTable.createdAt).epochSecond),
		parseDateColumn(get(RequestTable.modifiedAt).epochSecond)
	)
}
