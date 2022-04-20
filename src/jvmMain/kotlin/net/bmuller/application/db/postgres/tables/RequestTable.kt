package net.bmuller.application.db.postgres.tables

import net.bmuller.application.db.postgres.util.PGEnum
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

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

	val ENUM_TYPES = mapOf("RequestStatusEnum" to RequestStatus.values().map { value -> value.toString() })

	val dateCancelled: Column<Instant?> = timestamp("date_cancelled").nullable()
	val dateFulfilled: Column<Instant?> = timestamp("date_fulfilled").nullable()
	val dateRejected: Column<Instant?> = timestamp("date_rejected").nullable()
	val dateRequested: Column<Instant> = timestamp("date_requested").default(Instant.now())
	val rejectionReason: Column<String?> = text("rejection_reason").nullable()
	val status = customEnumeration("status",
		"RequestStatusEnum",
		{ value -> RequestStatus.valueOf(value as String) },
		{ PGEnum("RequestStatusEnum", it) })
	val userId: Column<Int> = integer("user_id").references(UserTable.id).index("user_id_fk")
	val mediaId: Column<Int> = integer("media_item_id").references(MediaItemTable.id).index("media_item_id_fk")
}