package entities

import lib.ProviderLogoPath

@kotlinx.serialization.Serializable
data class Provider(
	val logoPath: ProviderLogoPath,
	val id: Int,
	val name: String
)