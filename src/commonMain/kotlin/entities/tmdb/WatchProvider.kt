package entities.tmdb

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WatchProviderWrapper(val results: WatchRegionWrapper)

@kotlinx.serialization.Serializable
data class WatchRegionWrapper(@SerialName("US") val us: WatchRegion? = null)

@kotlinx.serialization.Serializable
data class WatchRegion(
	val link: String,
	val ads: List<WatchProvider>? = null,
	val flatrate: List<WatchProvider>? = null,
	val free: List<WatchProvider>? = null
)

@kotlinx.serialization.Serializable
data class WatchProvider(
	@SerialName("display_priority") val displayPriority: Int,
	@SerialName("logo_path") val logoPath: String,
	@SerialName("provider_id") val providerId: Int,
	@SerialName("provider_name") val providerName: String,
)