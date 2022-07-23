package net.bmuller.application.lib


fun kotlinx.datetime.Instant.toJavaInstant(): java.time.Instant = java.time.Instant.ofEpochSecond(epochSeconds)

fun java.time.Instant.toKotlinInstant(): kotlinx.datetime.Instant =
	kotlinx.datetime.Instant.fromEpochSeconds(this.epochSecond)