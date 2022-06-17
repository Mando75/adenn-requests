package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.effect
import arrow.core.left
import arrow.core.right
import entities.RequestEntity
import entities.SearchResult
import entities.UserEntity
import net.bmuller.application.entities.UserSession
import net.bmuller.application.repository.RequestsRepository
import net.bmuller.application.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject

sealed class RequestServiceErrors {
	object UserNotFound : RequestServiceErrors()
}

class RequestService : BaseService() {
	private val requestsRepository: RequestsRepository by inject(RequestsRepository::class.java)
	private val usersRepository: UserRepository by inject(UserRepository::class.java)

	suspend fun submitRequest(result: SearchResult, session: UserSession) =
		effect<RequestServiceErrors, RequestEntity> {
			val user = getUser(session.id).bind()
			val newRequest = RequestEntity.fromSearchResult(result)
			val request = requestsRepository.createAndReturnRequest(newRequest, user, true)
			submitRequestToJobQueue(request)
			return@effect request
		}.toEither()

	private suspend fun getUser(userId: Int): Either<RequestServiceErrors.UserNotFound, UserEntity> {
		return usersRepository.getUserById(userId)?.right() ?: RequestServiceErrors.UserNotFound.left()
	}

	private fun submitRequestToJobQueue(request: RequestEntity) {
		// TODO: submit request to job queue
	}
}