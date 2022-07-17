package matchers

import io.kotest.common.runBlocking
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.bmuller.application.lib.GenericErrorModel


infix fun HttpResponse.shouldHaveHttpStatus(httpStatusCode: HttpStatusCode) =
	this should haveHttpStatus(httpStatusCode.value)

fun haveHttpStatus(expected: Int) = object : Matcher<HttpResponse> {
	override fun test(value: HttpResponse): MatcherResult {
		return MatcherResult(
			value.status.value == expected,
			{ runBlocking { "Response should have status $expected but had status ${value.status.value}. Response body: ${value.body<GenericErrorModel>()}" } },
			{ runBlocking { "Response should not have status $expected. Response body: ${value.body<GenericErrorModel>()}" } }
		)

	}
}
