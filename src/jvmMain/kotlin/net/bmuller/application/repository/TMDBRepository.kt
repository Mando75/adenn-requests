package net.bmuller.application.repository

import arrow.core.Either
import entities.tmdb.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import net.bmuller.application.http.TMDBClient
import net.bmuller.application.lib.Unknown
import net.bmuller.application.lib.catchUnknown


@kotlinx.serialization.Serializable
@Resource("/3")
private class TMDBAPIResource {
	@kotlinx.serialization.Serializable
	@Resource("search")
	class Search(@Suppress("unused") val parent: TMDBAPIResource = TMDBAPIResource()) {

		@kotlinx.serialization.Serializable
		@Resource("movie")
		class Movie(@Suppress("unused") val parent: Search = Search(), val query: String, val adult: Boolean = false)

		@kotlinx.serialization.Serializable
		@Resource("tv")
		class TV(@Suppress("unused") val parent: Search = Search(), val query: String, val adult: Boolean = false)

		@kotlinx.serialization.Serializable
		@Resource("multi")
		class Multi(@Suppress("unused") val parent: Search = Search(), val query: String, val adult: Boolean = false)
	}

	@kotlinx.serialization.Serializable
	@Resource("configuration")
	class Configuration(@Suppress("unused") val parent: TMDBAPIResource = TMDBAPIResource())


	@kotlinx.serialization.Serializable
	@Resource("tv")
	class TV(@Suppress("unused") val parent: TMDBAPIResource = TMDBAPIResource()) {
		@kotlinx.serialization.Serializable
		@Resource("{id}")
		class Id(
			@Suppress("unused")
			val parent: TV = TV(),
			val id: Int,
			@SerialName("append_to_response") val appendToResponse: String? = null,
		) {

			@kotlinx.serialization.Serializable
			@Resource("watch/providers")
			class WatchProviders(val id: Int, @Suppress("unused") val parent: Id = Id(id = id))
		}
	}

	@kotlinx.serialization.Serializable
	@Resource("movie")
	class Movie(@Suppress("unused") val parent: TMDBAPIResource = TMDBAPIResource()) {
		@kotlinx.serialization.Serializable
		@Resource("{id}")
		class Id(
			@Suppress("unused")
			val parent: Movie = Movie(),
			val id: Int,
			@SerialName("append_to_response") val appendToResponse: String? = null,
		) {
			@kotlinx.serialization.Serializable
			@Resource("watch/providers")
			class WatchProviders(val id: Int, @Suppress("unused") val parent: Id = Id(id = id))
		}
	}
}

interface TMDBRepository {
	suspend fun searchMovies(query: String): Either<Unknown, MovieSearchResults>
	suspend fun searchTVShows(query: String): Either<Unknown, TVShowSearchResults>

	suspend fun searchMulti(query: String): Either<Unknown, MultiSearchResults>
	suspend fun getConfiguration(): Either<Unknown, TMDBConfigurationResult>

	suspend fun movieDetail(id: Int): Either<Unknown, MovieDetail>

	suspend fun tvDetail(id: Int): Either<Unknown, TVShowDetail>

	suspend fun movieProviders(id: Int): Either<Unknown, WatchProviderWrapper>

	suspend fun tvProviders(id: Int): Either<Unknown, WatchProviderWrapper>
}

fun tmdbRepository(tmdb: TMDBClient) = object : TMDBRepository {
	override suspend fun searchMovies(query: String): Either<Unknown, MovieSearchResults> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.Movie(query = query))
		response.body()
	}

	override suspend fun searchTVShows(query: String): Either<Unknown, TVShowSearchResults> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.TV(query = query))
		response.body()
	}

	override suspend fun searchMulti(query: String): Either<Unknown, MultiSearchResults> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.Multi(query = query))
		response.body()
	}

	override suspend fun getConfiguration(): Either<Unknown, TMDBConfigurationResult> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Configuration())
		response.body()
	}

	override suspend fun movieDetail(id: Int): Either<Unknown, MovieDetail> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Movie.Id(id = id))
		response.body()
	}

	override suspend fun tvDetail(id: Int): Either<Unknown, TVShowDetail> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.TV.Id(id = id))
		response.body()
	}

	override suspend fun movieProviders(id: Int): Either<Unknown, WatchProviderWrapper> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.Movie.Id.WatchProviders(id = id))
		response.body()
	}

	override suspend fun tvProviders(id: Int): Either<Unknown, WatchProviderWrapper> = Either.catchUnknown {
		val response = tmdb.client.get(resource = TMDBAPIResource.TV.Id.WatchProviders(id = id))
		response.body()
	}
}