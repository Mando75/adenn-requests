package net.bmuller.application.repository

import db.tables.RequestTable
import db.tables.UserTable
import entities.RequestEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface RequestsRepository {
	suspend fun createAndReturnRequest(newRequest: RequestEntity): RequestEntity
}


class RequestsRepositoryImpl : BaseRepository(), RequestsRepository {
	override suspend fun createAndReturnRequest(newRequest: RequestEntity): RequestEntity {
		return newSuspendedTransaction(Dispatchers.IO, db) {
			val id = RequestTable.insertAndGetId { request ->
				request[tmdbId] = newRequest.tmdbId
				request[mediaType] =
					if (newRequest is RequestEntity.MovieRequest) RequestTable.MediaType.MOVIE else RequestTable.MediaType.TV
				request[title] = newRequest.title
				request[posterPath] = newRequest.posterPath
				request[status] = newRequest.status
				request[requesterId] = newRequest.requester.id
			}

			val result = (RequestTable innerJoin UserTable).select { RequestTable.id eq id }.single()
			println(result)

			newRequest
		}

	}


}