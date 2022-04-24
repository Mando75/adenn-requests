package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.computations.either
import io.ktor.client.request.*
import io.ktor.http.*
import net.bmuller.application.http.tmdb.MovieSearchResults
import net.bmuller.application.http.tmdb.TMDBClientError
import net.bmuller.application.http.tmdb.TMDBConfigurationResult
import net.bmuller.application.http.tmdb.TVShowSearchResults
import java.net.URL

interface TMDBRepository {
	suspend fun searchMovies(query: String): Either<TMDBClientError, MovieSearchResults>
	suspend fun searchTVShows(query: String): Either<TMDBClientError, TVShowSearchResults>
	suspend fun getConfiguration(): Either<TMDBClientError, TMDBConfigurationResult>
}

class TMDBRepositoryImpl : BaseRepository(), TMDBRepository {

	private val BASE_URL = "https://api.themoviedb.org/3"


	override suspend fun searchMovies(query: String): Either<TMDBClientError, MovieSearchResults> = either {
		val request = HttpRequestBuilder(url = URL("$BASE_URL/search/movie"))
		request.method = HttpMethod.Get
		request.parameter("query", query)
		val (searchResults) = tmdbClient.exec<MovieSearchResults>(request).bind()
		return@either searchResults
	}

	override suspend fun searchTVShows(query: String): Either<TMDBClientError, TVShowSearchResults> = either {
		val request = HttpRequestBuilder(url = URL("$BASE_URL/search/tv"))
		request.method = HttpMethod.Get
		request.parameter("query", query)
		val (searchResults) = tmdbClient.exec<TVShowSearchResults>(request).bind()
		return@either searchResults
	}

	override suspend fun getConfiguration(): Either<TMDBClientError, TMDBConfigurationResult> = either {
		val request = HttpRequestBuilder(url = URL("$BASE_URL/configuration"))
		request.method = HttpMethod.Get
		val (configuration) = tmdbClient.exec<TMDBConfigurationResult>(request).bind()
		return@either configuration
	}
}