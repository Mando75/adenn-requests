package net.bmuller.application.repository

import db.tables.RequestTable
import db.tables.UserTable
import db.tables.toRequestEntity
import db.tables.toUserEntity
import entities.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant


interface RequestsRepository {
	suspend fun createAndReturnRequest(
		newRequest: RequestEntity,
		requester: UserEntity,
		includeRequester: Boolean = false
	): RequestEntity

	suspend fun getQuotaUsage(userId: Int, timePeriod: Instant, isMovie: Boolean): Long

	suspend fun requestsByTMDBId(tmdbIds: List<Int>): Map<Int, RequestEntity>

	suspend fun requests(filters: RequestFilters, pagination: Pagination): PaginatedResponse<RequestEntity>
}


class RequestsRepositoryImpl : BaseRepository(), RequestsRepository {
	override suspend fun createAndReturnRequest(
		newRequest: RequestEntity,
		requester: UserEntity,
		includeRequester: Boolean
	): RequestEntity {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val id = RequestTable.insertAndGetId { request ->
				request[tmdbId] = newRequest.tmdbId
				request[mediaType] =
					if (newRequest is RequestEntity.MovieRequest) RequestTable.MediaType.MOVIE else RequestTable.MediaType.TV
				request[title] = newRequest.title
				request[posterPath] = newRequest.posterPath
				request[status] = newRequest.status
				request[requesterId] = requester.id
			}

			UserTable.update({ UserTable.id eq requester.id }) {
				with(SqlExpressionBuilder) {
					it[requestCount] = requestCount + 1
				}
			}

			val table = if (includeRequester) {
				(RequestTable innerJoin UserTable)
			} else RequestTable

			val result = table.select { RequestTable.id eq id }.single()
			val user = if (includeRequester) {
				result.toUserEntity()
			} else null

			return@newSuspendedTransaction result.toRequestEntity(user)
		}
	}

	override suspend fun getQuotaUsage(userId: Int, timePeriod: Instant, isMovie: Boolean): Long {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val mediaType = if (isMovie) RequestTable.MediaType.MOVIE else RequestTable.MediaType.TV
			return@newSuspendedTransaction RequestTable
				.select { RequestTable.requesterId eq userId }
				.andWhere { RequestTable.mediaType eq mediaType }
				.andWhere { RequestTable.createdAt greaterEq timePeriod }
				.count()
		}
	}

	override suspend fun requestsByTMDBId(tmdbIds: List<Int>): Map<Int, RequestEntity> {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			RequestTable
				.select { RequestTable.tmdbId inList tmdbIds }
				.associate { row -> row[RequestTable.tmdbId] to row.toRequestEntity() }
		}
	}

	override suspend fun requests(filters: RequestFilters, pagination: Pagination): PaginatedResponse<RequestEntity> {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val query = RequestTable.selectAll()
			filters.status?.let { status -> query.andWhere { RequestTable.status inList status } }


			val count = query.count()
			val rows = query
				.limit(pagination.limit, offset = pagination.offset)

			val requests = rows.mapNotNull { row -> row.toRequestEntity() }
			return@newSuspendedTransaction PaginatedResponse(
				items = requests,
				totalCount = count,
				limit = pagination.limit,
				offset = pagination.offset
			)
		}
	}
}