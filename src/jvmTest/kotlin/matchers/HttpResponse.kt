package matchers

import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.ktor.client.statement.*
import io.ktor.http.*


infix fun HttpResponse.shouldHaveHttpStatus(httpStatusCode: HttpStatusCode) =
	this.shouldHaveStatus(httpStatusCode)

