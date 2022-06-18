package http

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/requests")
class RequestResource