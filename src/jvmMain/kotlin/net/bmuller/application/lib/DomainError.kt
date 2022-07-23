package net.bmuller.application.lib

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

sealed interface DomainError

object Unauthorized : DomainError

object QuotaExceeded : DomainError

data class TimedOut(val message: String) : DomainError
data class Forbidden(val message: String = "Forbidden") : DomainError
data class Unknown(val description: String, val error: Throwable? = null) : DomainError
data class InvalidBody(val description: String, val error: Throwable) : DomainError
data class MissingOrInvalidParam(val description: String) : DomainError
data class EntityNotFound(val entityId: String, val message: String = "Could not find entity $entityId") : DomainError


suspend fun PipelineContext<Unit, ApplicationCall>.respondError(error: DomainError): Unit = when (error) {
	is Unauthorized -> unauthorized()
	is EntityNotFound -> notFound(error.message)
	is Forbidden -> forbidden(error.message)
	is QuotaExceeded -> unprocessable("Quota Exceeded")
	is TimedOut -> timeout(error.message)
	is Unknown -> internal(
		"""
		An unknown error occurred:
		  - description: ${error.description}
		  - error: ${error.error}
	""".trimIndent()
	)
	is MissingOrInvalidParam -> badRequest(
		"""
		Error parsing param:
		 - description: ${error.description}
	""".trimIndent()
	)
	is InvalidBody -> unprocessable(
		"""
		Error parsing request body:
		  - description: ${error.description}
		  - error: ${error.error}
	""".trimIndent()
	)
}