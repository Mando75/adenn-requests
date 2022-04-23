package net.bmuller.application.repository

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import io.ktor.client.request.*
import io.ktor.http.*
import net.bmuller.application.config.MissingEnvException
import net.bmuller.application.http.tmdb.MovieSearchResults
import net.bmuller.application.http.tmdb.TMDBConfigurationResult
import net.bmuller.application.http.tmdb.TVShowSearchResults
import java.net.URL

interface TMDBRepository {
	suspend fun searchMovies(query: String): Either<TMDBRepositoryErrors, MovieSearchResults>
	suspend fun searchTVShows(query: String): Either<TMDBRepositoryErrors, TVShowSearchResults>
	suspend fun getConfiguration(): Either<TMDBRepositoryErrors, TMDBConfigurationResult>
}

sealed class TMDBRepositoryErrors {
	data class InvalidConfig(val missingKey: String) : TMDBRepositoryErrors()
	object Unknown : TMDBRepositoryErrors()
}

data class TMDBRepositoryConfiguration(val baseUrl: String)

class TMDBRepositoryImpl : BaseRepository(), TMDBRepository {

	/**
	 * Extract the values we need from the config provider
	 */
	private fun getTMDBConfig(): Either<MissingEnvException, TMDBRepositoryConfiguration> = either.eager {
		val baseUrl = configProvider.getValue("MOVIE_DB_BASE_URL").bind()

		return@eager TMDBRepositoryConfiguration(baseUrl)
	}

	/**
	 * Helper for monad comprehension
	 */
	private val handleMissingEnvException: (MissingEnvException) -> Either.Left<TMDBRepositoryErrors> =
		{ envException ->
			when (envException) {
				is MissingEnvException.NullKey -> Either.Left(TMDBRepositoryErrors.InvalidConfig(envException.missingKey))
			}
		}

	override suspend fun searchMovies(query: String): Either<TMDBRepositoryErrors, MovieSearchResults> {
		return getTMDBConfig()
			.mapLeft(handleMissingEnvException)
			.flatMap { config ->
				val request = HttpRequestBuilder(url = URL("${config.baseUrl}/search/movie"))
				request.method = HttpMethod.Get
				request.parameter("query", query)
				tmdbClient.exec<MovieSearchResults>(request)

			}
			.mapLeft { TMDBRepositoryErrors.Unknown }
			.map { (searchResults) -> searchResults }
	}

	override suspend fun searchTVShows(query: String): Either<TMDBRepositoryErrors, TVShowSearchResults> {
		TODO("Not yet implemented")
	}

	override suspend fun getConfiguration(): Either<TMDBRepositoryErrors, TMDBConfigurationResult> {
		TODO("Not yet implemented")
	}
}