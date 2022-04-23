package net.bmuller.application.repository

import net.bmuller.application.entities.Request

interface RequestsRepository {
	suspend fun insertRequest(request: Request)
}

class RequestsRepositoryImpl : BaseRepository(), RequestsRepository {
	override suspend fun insertRequest(request: Request) {
		TODO("Not yet implemented")
	}
}