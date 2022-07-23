@file:OptIn(ExperimentalTime::class)

package net.bmuller.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.logging.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource


private val CALL_START_TIME = AttributeKey<TimeMark>("CallStartTime")
fun Application.configureLogging() {
	intercept(ApplicationCallPipeline.Setup) {
		call.attributes.put(CALL_START_TIME, TimeSource.Monotonic.markNow())
	}

	install(CallLogging) {
		filter { call -> call.request.path().startsWith("/api") }
		format { call ->
			val time = when (val startTime = call.attributes.getOrNull(CALL_START_TIME)) {
				null -> ""
				else -> startTime.elapsedNow().toString()
			}
			when (val status = call.response.status() ?: "Unhandled") {
				HttpStatusCode.Found -> "$status: ${call.request.toLogString()} -> ${call.response.headers[HttpHeaders.Location]} $time"
				else -> "$status: ${call.request.toLogString()} $time"
			}
		}
	}
}