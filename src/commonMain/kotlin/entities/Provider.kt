package entities

@kotlinx.serialization.Serializable
data class Provider(
	val logoPath: String,
	val id: Int,
	val name: String
)