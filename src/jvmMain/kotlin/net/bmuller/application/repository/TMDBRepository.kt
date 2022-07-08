package net.bmuller.application.repository

import arrow.core.Either
import entities.tmdb.MovieSearchResults
import entities.tmdb.MultiSearchResults
import entities.tmdb.TMDBConfigurationResult
import entities.tmdb.TVShowSearchResults
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.resources.*


@Suppress("unused")
@kotlinx.serialization.Serializable
@Resource("/3")
private class TMDBAPIResource {
	@kotlinx.serialization.Serializable
	@Resource("search")
	class Search(val parent: TMDBAPIResource = TMDBAPIResource()) {

		@kotlinx.serialization.Serializable
		@Resource("movie")
		class Movie(val parent: Search = Search(), val query: String, val adult: Boolean = false)

		@kotlinx.serialization.Serializable
		@Resource("tv")
		class TV(val parent: Search = Search(), val query: String, val adult: Boolean = false)

		@kotlinx.serialization.Serializable
		@Resource("multi")
		class Multi(val parent: Search = Search(), val query: String, val adult: Boolean = false)
	}

	@kotlinx.serialization.Serializable
	@Resource("configuration")
	class Configuration(val parent: TMDBAPIResource = TMDBAPIResource())
}

interface TMDBRepository {
	suspend fun searchMovies(query: String): Either<Throwable, MovieSearchResults>
	suspend fun searchTVShows(query: String): Either<Throwable, TVShowSearchResults>

	suspend fun searchMulti(query: String): Either<Throwable, MultiSearchResults>
	suspend fun getConfiguration(): Either<Throwable, TMDBConfigurationResult>
}

class TMDBRepositoryImpl : BaseRepository(), TMDBRepository {

	override suspend fun searchMovies(query: String): Either<Throwable, MovieSearchResults> = Either.catch {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.Movie(query = query))
		return@catch response.body()
	}

	override suspend fun searchTVShows(query: String): Either<Throwable, TVShowSearchResults> = Either.catch {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.TV(query = query))
		return@catch response.body()
	}

	override suspend fun searchMulti(query: String): Either<Throwable, MultiSearchResults> = Either.catch {
		val response = tmdb.client.get(resource = TMDBAPIResource.Search.Multi(query = query))
		return@catch response.body()
	}

	override suspend fun getConfiguration(): Either<Throwable, TMDBConfigurationResult> = Either.catch {
		val response = tmdb.client.get(resource = TMDBAPIResource.Configuration())
		return@catch response.body()
	}

}