package entities.tmdb

import entities.Provider
import kotlinx.serialization.SerialName
import lib.ImageTools

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

fun WatchProviderWrapper.unwrapToProviders(allowedProviders: List<Pair<Int, String>>): List<Provider> {
	val tmdbProviders = this.results.us
	val providerIds = allowedProviders.map { it.first }.toSet()
	val predicate: (provider: WatchProvider) -> Boolean = { provider -> providerIds.contains(provider.providerId) }

	val ads = tmdbProviders?.ads?.filter(predicate) ?: emptyList()
	val flatrate = tmdbProviders?.flatrate?.filter(predicate) ?: emptyList()
	val free = tmdbProviders?.free?.filter(predicate) ?: emptyList()
	val combined = ads + flatrate + free

	return combined.map { provider ->
		Provider(
			id = provider.providerId,
			name = provider.providerName,
			logoPath = ImageTools.providerLogoPath(provider.logoPath)
		)
	}
}