package net.bmuller.application.repository

import arrow.core.Either
import db.tables.RequestTable
import db.tables.UserTable
import db.util.ilike
import entities.Pagination
import entities.RequestFilterMediaType
import entities.RequestFilters
import entities.RequestStatus
import kotlinx.coroutines.Dispatchers
import net.bmuller.application.lib.DomainError
import net.bmuller.application.lib.catchUnknown
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

data class CreatedRequestData(val id: Int)
data class RequestByTmdbIDData(val id: Int, val status: RequestStatus)

data class RequestList(val requests: List<RequestListData>, val totalCount: Long)
data class RequestListData(
	val id: Int,
	val tmdbId: Int,
	val title: String,
	val status: RequestStatus,
	val mediaType: MediaType
) {
	enum class MediaType {
		MOVIE,
		TV;

		companion object {
			fun fromTableMediaType(type: RequestTable.MediaType) = when (type) {
				RequestTable.MediaType.MOVIE -> MOVIE
				RequestTable.MediaType.TV -> TV
			}
		}
	}
}

interface RequestsRepository {
	suspend fun createRequest(
		title: String,
		tmdbId: Int,
		mediaType: RequestTable.MediaType,
		requesterId: Int
	): Either<DomainError, CreatedRequestData>

	suspend fun getQuotaUsage(
		userId: Int,
		timePeriod: Instant,
		mediaType: RequestTable.MediaType
	): Either<DomainError, Long>

	suspend fun findByTmdbId(tmdbIds: List<Int>): Either<DomainError, Map<Int, RequestByTmdbIDData>>

	suspend fun requests(
		filters: RequestFilters,
		pagination: Pagination
	): Either<DomainError, RequestList>
}

fun requestsRepository(exposed: Database) = object : RequestsRepository {
	override suspend fun createRequest(
		title: String,
		tmdbId: Int,
		mediaType: RequestTable.MediaType,
		requesterId: Int
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
		userId: Int,
		timePeriod: Instant,
		mediaType: RequestTable.MediaType
	): Either<DomainError, Long> =
		Either.catchUnknown {
			newSuspendedTransaction(Dispatchers.IO, exposed) {
				return@newSuspendedTransaction RequestTable
					.select { RequestTable.requesterId eq userId }
					.andWhere { RequestTable.mediaType eq mediaType }
					.andWhere { RequestTable.createdAt greaterEq timePeriod }
					.count()
			}
		}

	override suspend fun findByTmdbId(tmdbIds: List<Int>): Either<DomainError, Map<Int, RequestByTmdbIDData>> =
		Either.catchUnknown {
			newSuspendedTransaction(Dispatchers.IO, exposed) {
				RequestTable
					.innerJoin(UserTable)
					.slice(RequestTable.id, RequestTable.status, RequestTable.tmdbId)
					.select { RequestTable.tmdbId inList tmdbIds }
					.associate { row ->
						row[RequestTable.tmdbId] to RequestByTmdbIDData(
							id = row[RequestTable.id].value,
							status = row[RequestTable.status]
						)
					}
			}
		}

	override suspend fun requests(
		filters: RequestFilters,
		pagination: Pagination
	): Either<DomainError, RequestList> = Either.catchUnknown {
		newSuspendedTransaction(Dispatchers.IO, exposed) {
			val mediaType = when (filters.mediaType) {
				RequestFilterMediaType.ALL -> null
				RequestFilterMediaType.MOVIE -> RequestTable.MediaType.MOVIE
				RequestFilterMediaType.TV -> RequestTable.MediaType.TV
			}
			val searchTerm = filters.searchTerm?.ifBlank { null } // don't pass empty strings

			val query =
				RequestTable
					.slice(
						RequestTable.id,
						RequestTable.tmdbId,
						RequestTable.title,
						RequestTable.status,
						RequestTable.mediaType
					)
					.selectAll()

			mediaType?.let { type -> { RequestTable.mediaType eq type } }
			filters.status?.let { status -> query.andWhere { RequestTable.status inList status } }
			searchTerm?.let { term -> query.andWhere { RequestTable.title ilike "$term%" } }


			val count = query.count()
			val rows = query
				.limit(pagination.limit, offset = pagination.offset)

			val requests = rows.mapNotNull { row ->
				RequestListData(
					id = row[RequestTable.id].value,
					tmdbId = row[RequestTable.tmdbId],
					title = row[RequestTable.title],
					status = row[RequestTable.status],
					mediaType = RequestListData.MediaType.fromTableMediaType(row[RequestTable.mediaType])
				)
			}

			return@newSuspendedTransaction RequestList(requests, count)
		}
	}
}