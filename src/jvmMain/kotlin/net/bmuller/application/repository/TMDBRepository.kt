package net.bmuller.application.repository

import arrow.core.Either
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.resources.*
import net.bmuller.application.http.tmdb.MovieSearchResults
import net.bmuller.application.http.tmdb.TMDBConfigurationResult
import net.bmuller.application.http.tmdb.TVShowSearchResults


@kotlinx.serialization.Serializable
@Resource("/3")
class TMDBResources() {
	@kotlinx.serialization.Serializable
	@Resource("search")
	class Search(val parent: TMDBResources = TMDBResources()) {

		@kotlinx.serialization.Serializable
		@Resource("movie")
		class Movie(val parent: Search = Search(), val query: String)

		@kotlinx.serialization.Serializable
		@Resource("tv")
		class TV(val parent: Search = Search(), val query: String)
	}

	@kotlinx.serialization.Serializable
	@Resource("configuration")
	class Configuration(val parent: TMDBResources = TMDBResources())
}

interface TMDBRepository {
	suspend fun searchMovies(query: String): Either<Throwable, MovieSearchResults>
	suspend fun searchTVShows(query: String): Either<Throwable, TVShowSearchResults>
	suspend fun getConfiguration(): Either<Throwable, TMDBConfigurationResult>
}

class TMDBRepositoryImpl : BaseRepository(), TMDBRepository {

	override suspend fun searchMovies(query: String): Either<Throwable, MovieSearchResults> = Either.catch {
		val response = tmdb.client.get(resource = TMDBResources.Search.Movie(query = query))
		return@catch response.body<MovieSearchResults>()
	}

	override suspend fun searchTVShows(query: String): Either<Throwable, TVShowSearchResults> = Either.catch {
		val response = tmdb.client.get(resource = TMDBResources.Search.TV(query = query))
		return@catch response.body<TVShowSearchResults>()
	}

	override suspend fun getConfiguration(): Either<Throwable, TMDBConfigurationResult> = Either.catch {
		val response = tmdb.client.get(resource = TMDBResources.Configuration())
		return@catch response.body<TMDBConfigurationResult>()
	}

}