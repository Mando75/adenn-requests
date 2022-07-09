package net.bmuller.application.lib

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import net.bmuller.application.lib.error.*

@Serializable
data class GenericErrorModel(val errors: GenericErrorModelErrors)

@Serializable
data class GenericErrorModelErrors(val body: List<String>)

fun GenericErrorModel(vararg msg: String): GenericErrorModel =
	GenericErrorModel(GenericErrorModelErrors(msg.toList()))

suspend inline fun <reified A : Any> PipelineContext<Unit, ApplicationCall>.receiveCatching(): Either<InvalidBody, A> =
	Either.catch {
		call.receive<A>()
	}.mapLeft { e -> InvalidBody("Received malformed JSON for ${A::class.simpleName}", e) }

context (PipelineContext<Unit, ApplicationCall>)

		suspend inline fun <reified A : Any> Either<DomainError, A>.respond(status: HttpStatusCode = HttpStatusCode.OK): Unit =
	when (this) {
		is Either.Left -> respondError(value)
		is Either.Right -> call.respond(status, value)
	}

suspend fun PipelineContext<Unit, ApplicationCall>.respondError(error: DomainError): Unit = when (error) {
	is Unauthorized -> unauthorized()
	is EntityNotFound -> notFound(error.entityId)
	is Unknown -> internal(
		"""
		An unknown error occurred:
		  - description: ${error.description}
		  - error: ${error.error}
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

private suspend inline fun PipelineContext<Unit, ApplicationCall>.unprocessable(
	error: String
): Unit = call.respond(HttpStatusCode.UnprocessableEntity, GenericErrorModel(error))

private suspend inline fun PipelineContext<Unit, ApplicationCall>.internal(error: String): Unit =
	call.respond(HttpStatusCode.InternalServerError, GenericErrorModel(error))

private suspend inline fun PipelineContext<Unit, ApplicationCall>.unauthorized(): Unit =
	call.respond(HttpStatusCode.Unauthorized, GenericErrorModel("Unauthorized"))

private suspend inline fun PipelineContext<Unit, ApplicationCall>.notFound(entityId: String): Unit = call.respond(
	HttpStatusCode.NotFound,
	GenericErrorModel("Entity with id $entityId not found")
)
