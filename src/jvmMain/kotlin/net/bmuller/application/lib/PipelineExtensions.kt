package net.bmuller.application.lib

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

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

inline fun <reified A : Principal> PipelineContext<Unit, ApplicationCall>.principalCatching(): Either<Unauthorized, A> =
	Either.catch {
		call.principal<A>() ?: throw Exception("Unauthorized")
	}.mapLeft { Unauthorized }

context (PipelineContext<Unit, ApplicationCall>)

		suspend inline fun <reified A : Any> Either<DomainError, A>.respond(status: HttpStatusCode = HttpStatusCode.OK): Unit =
	when (this) {
		is Either.Left -> respondError(value)
		is Either.Right -> call.respond(status, value)
	}

context (PipelineContext<Unit, ApplicationCall>)
		suspend inline fun <reified A : Any> Either<DomainError, A>.respondRedirect(destination: String): Unit =
	when (this) {
		is Either.Left -> respondError(value)
		is Either.Right -> call.respondRedirect(destination)
	}

suspend inline fun PipelineContext<Unit, ApplicationCall>.unprocessable(
	error: String
): Unit = call.respond(HttpStatusCode.UnprocessableEntity, GenericErrorModel(error))

suspend inline fun PipelineContext<Unit, ApplicationCall>.internal(error: String): Unit =
	call.respond(HttpStatusCode.InternalServerError, GenericErrorModel(error))

suspend inline fun PipelineContext<Unit, ApplicationCall>.unauthorized(): Unit =
	call.respond(HttpStatusCode.Unauthorized, GenericErrorModel("Unauthorized"))

suspend inline fun PipelineContext<Unit, ApplicationCall>.notFound(message: String): Unit = call.respond(
	HttpStatusCode.NotFound,
	GenericErrorModel(message)
)

suspend inline fun PipelineContext<Unit, ApplicationCall>.forbidden(message: String): Unit = call.respond(
	HttpStatusCode.Forbidden, GenericErrorModel(message)
)

suspend inline fun PipelineContext<Unit, ApplicationCall>.timeout(message: String): Unit =
	call.respond(HttpStatusCode.RequestTimeout, GenericErrorModel(message))

suspend inline fun PipelineContext<Unit, ApplicationCall>.badRequest(message: String): Unit =
	call.respond(HttpStatusCode.BadRequest, GenericErrorModel(message))