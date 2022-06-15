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
		CANCELLED,
		WAITING,
		IMPORTED,
		DOWNLOADING
	}


	val dateCancelled: Column<Instant?> = timestamp("date_cancelled").nullable()
	val dateFulfilled: Column<Instant?> = timestamp("date_fulfilled").nullable()
	val dateRejected: Column<Instant?> = timestamp("date_rejected").nullable()
	val dateRequested: Column<Instant> = timestamp("date_requested").default(Instant.now())
	val rejectionReason: Column<String?> = text("rejection_reason").nullable()
	val status: Column<RequestStatus> = postgresEnumeration("status", "Request_Status_Enum")
	val userId: Column<Int> = integer("user_id").references(UserTable.id).index("user_id_fk")
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())
}