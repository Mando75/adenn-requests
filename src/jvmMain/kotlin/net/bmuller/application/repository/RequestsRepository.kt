package net.bmuller.application.repository

import db.tables.RequestTable
import db.tables.toRequestEntity
import entities.RequestEntity
import entities.UserEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface RequestsRepository {
	suspend fun createAndReturnRequest(newRequest: RequestEntity, requester: UserEntity): RequestEntity
}


class RequestsRepositoryImpl : BaseRepository(), RequestsRepository {
	override suspend fun createAndReturnRequest(newRequest: RequestEntity, requester: UserEntity): RequestEntity {
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

			val result = RequestTable.select { RequestTable.id eq id }.single()

			return@newSuspendedTransaction result.toRequestEntity()
		}

	}


}