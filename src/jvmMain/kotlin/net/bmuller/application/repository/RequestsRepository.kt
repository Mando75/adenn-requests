package net.bmuller.application.repository

import arrow.core.Either
import db.tables.RequestTable
import db.tables.UserTable
import db.util.ilike
import entities.*
import kotlinx.coroutines.Dispatchers
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.catchUnknown
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

data class CreatedRequestData(val id: Int)
data class RequestByTmdbIdData(val id: Int, val status: RequestStatus)

data class RequestList(val requests: List<RequestListData>, val totalCount: Long)
data class RequestListData(
	val id: Int,
	val tmdbId: Int,
	val title: String,
	val status: RequestStatus,
	val mediaType: MediaType,
	val createdAt: Instant,
	val modifiedAt: Instant,
	val requester: Requester
) {
	data class Requester(val id: Int, val username: String, val profilePicUrl: String?)
}

data class RequestByIdData(
	val id: Int,
	val tmdbId: Int,
	val title: String,
	val status: RequestStatus,
	val mediaType: MediaType,
	val createdAt: Instant,
	val modifiedAt: Instant,
	val requesterId: Int,
	val rejectionReason: String?
)

interface RequestsRepository {
	suspend fun createRequest(
		title: String, tmdbId: Int, mediaType: MediaType, requesterId: Int
	): Either<DomainError, CreatedRequestData>

	suspend fun getQuotaUsage(
		userId: Int, timePeriod: Instant, mediaType: MediaType
	): Either<DomainError, Long>

	suspend fun findByTmdbId(tmdbIds: List<Int>): Either<DomainError, Map<Int, RequestByTmdbIdData>>

	suspend fun findById(requestId: Int): Either<DomainError, RequestByIdData>

	suspend fun requests(
		filters: RequestFilters, pagination: Pagination
	): Either<DomainError, RequestList>

	suspend fun updateRequestStatus(
		requestId: Int,
		status: RequestStatus,
		rejectionReason: String? = null,
		dateFulfilled: Instant? = null,
		dateRejected: Instant? = null,
	): Either<DomainError, Int>
}

fun requestsRepository(exposed: Database) = object : RequestsRepository {
	override suspend fun createRequest(
		title: String, tmdbId: Int, mediaType: MediaType, requesterId: Int
	): Either<DomainError, CreatedRequestData> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			val id = RequestTable.insertAndGetId { request ->
				request[RequestTable.title] = title
				request[RequestTable.tmdbId] = tmdbId
				request[RequestTable.mediaType] = mediaType
				request[RequestTable.requesterId] = requesterId
			}

			UserTable.update({ UserTable.id eq requesterId }) { userTable ->
				with(SqlExpressionBuilder) {
					userTable[requestCount] = requestCount + 1
				}
			}

			return@newSuspendedTransaction CreatedRequestData(id = id.value)
		}
	}

	override suspend fun getQuotaUsage(
		userId: Int, timePeriod: Instant, mediaType: MediaType
	): Either<DomainError, Long> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			return@newSuspendedTransaction RequestTable.select { RequestTable.requesterId eq userId }
				.andWhere { RequestTable.mediaType eq mediaType }
				.andWhere { RequestTable.createdAt greaterEq timePeriod }.count()
		}
	}

	override suspend fun findById(requestId: Int): Either<DomainError, RequestByIdData> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			val row = RequestTable
				.slice(
					RequestTable.id,
					RequestTable.tmdbId,
					RequestTable.title,
					RequestTable.status,
					RequestTable.mediaType,
					RequestTable.createdAt,
					RequestTable.modifiedAt,
					RequestTable.requesterId,
					RequestTable.rejectionReason
				)
				.select { RequestTable.id eq requestId }
				.first()

			return@newSuspendedTransaction RequestByIdData(
				id = row[RequestTable.id].value,
				tmdbId = row[RequestTable.tmdbId],
				title = row[RequestTable.title],
				status = row[RequestTable.status],
				mediaType = row[RequestTable.mediaType],
				createdAt = row[RequestTable.createdAt],
				modifiedAt = row[RequestTable.modifiedAt],
				requesterId = row[RequestTable.requesterId],
				rejectionReason = row[RequestTable.rejectionReason]
			)
		}
	}

	override suspend fun findByTmdbId(tmdbIds: List<Int>): Either<DomainError, Map<Int, RequestByTmdbIdData>> =
		Either.catchUnknown {
			newSuspendedTransaction(Dispatchers.IO, exposed) {
				RequestTable.innerJoin(UserTable).slice(RequestTable.id, RequestTable.status, RequestTable.tmdbId)
					.select { RequestTable.tmdbId inList tmdbIds }.associate { row ->
						row[RequestTable.tmdbId] to RequestByTmdbIdData(
							id = row[RequestTable.id].value, status = row[RequestTable.status]
						)
					}
			}
		}

	override suspend fun requests(
		filters: RequestFilters, pagination: Pagination
	): Either<DomainError, RequestList> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			val mediaType = when (filters.mediaType) {
				RequestFilterMediaType.ALL -> null
				RequestFilterMediaType.MOVIE -> MediaType.MOVIE
				RequestFilterMediaType.TV -> MediaType.TV
			}
			val searchTerm = filters.searchTerm?.ifBlank { null } // don't pass empty strings

			val query = RequestTable.innerJoin(UserTable).slice(
				RequestTable.id,
				RequestTable.tmdbId,
				RequestTable.title,
				RequestTable.status,
				RequestTable.mediaType,
				RequestTable.createdAt,
				RequestTable.modifiedAt,
				UserTable.id,
				UserTable.plexUsername,
				UserTable.plexProfilePicUrl,
			).selectAll()

			mediaType?.let { type -> { RequestTable.mediaType eq type } }
			filters.status?.let { status -> query.andWhere { RequestTable.status inList status } }
			searchTerm?.let { term -> query.andWhere { RequestTable.title ilike "$term%" } }


			val count = query.count()
			val rows = query.orderBy(RequestTable.createdAt).limit(pagination.limit, offset = pagination.offset)

			val requests = rows.mapNotNull { row ->
				RequestListData(
					id = row[RequestTable.id].value,
					tmdbId = row[RequestTable.tmdbId],
					title = row[RequestTable.title],
					status = row[RequestTable.status],
					mediaType = row[RequestTable.mediaType],
					createdAt = row[RequestTable.createdAt],
					modifiedAt = row[RequestTable.modifiedAt],
					requester = RequestListData.Requester(
						id = row[UserTable.id].value,
						username = row[UserTable.plexUsername],
						profilePicUrl = row[UserTable.plexProfilePicUrl]
					)
				)
			}

			return@newSuspendedTransaction RequestList(requests, count)
		}
	}

	override suspend fun updateRequestStatus(
		requestId: Int,
		status: RequestStatus,
		rejectionReason: String?,
		dateFulfilled: Instant?,
		dateRejected: Instant?
	): Either<DomainError, Int> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			RequestTable.update({ RequestTable.id eq requestId }) { table ->
				table[RequestTable.status] = status
				table[RequestTable.rejectionReason] = rejectionReason
				table[RequestTable.dateFulfilled] = dateFulfilled
				table[RequestTable.dateRejected] = dateRejected
			}
		}
	}
}