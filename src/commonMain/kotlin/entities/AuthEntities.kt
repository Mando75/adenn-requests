package entities

@kotlinx.serialization.Serializable
data class LoginUrlResponse(val loginUrl: String, val pinId: Long)
