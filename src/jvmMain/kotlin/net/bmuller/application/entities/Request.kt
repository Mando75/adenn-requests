package net.bmuller.application.entities

import db.tables.RequestTable
import java.time.Instant

@Suppress("unused")
data class Request(
	val dateCancelled: Instant?,
	val dateFulfilled: Instant?,
	val dateRejected: Instant?,
	val dateRequested: Instant,
	val id: Int,
	val mediaId: Int,
	val rejectionReason: String?,
	val status: RequestTable.RequestStatus,
	val userId: Int,
)