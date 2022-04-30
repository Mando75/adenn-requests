package net.bmuller.application.routing.v1

import io.ktor.resources.*

@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/requests")
class RequestResource {

	@kotlinx.serialization.Serializable
	@Resource("{id}")
	class Id(val parent: RequestResource, val id: Long)
}