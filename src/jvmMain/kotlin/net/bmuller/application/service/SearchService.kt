package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import entities.SearchResult
import entities.tmdb.BaseMovieEntity
import entities.tmdb.BaseTVShowEntity
import entities.tmdb.MultiSearchEntity
import lib.ImageTools
import net.bmuller.application.lib.DomainError
import net.bmuller.application.repository.RequestByTmdbIDData
import net.bmuller.application.repository.RequestsRepository
import net.bmuller.application.repository.TMDBRepository

interface ISearchService {
	suspend fun searchMulti(searchTerm: String): Either<DomainError, List<SearchResult>>

	suspend fun searchMovie(searchTerm: String): Either<DomainError, List<SearchResult.MovieResult>>

	suspend fun searchTV(searchTerm: String): Either<DomainError, List<SearchResult.TVResult>>
}

fun searchService(tmdbRepository: TMDBRepository, requestsRepository: RequestsRepository) = object : ISearchService {
	override suspend fun searchMulti(searchTerm: String): Either<DomainError, List<SearchResult>> = either {
		val tmdbResults = tmdbRepository.searchMulti(searchTerm).bind()
		val matchingRequests =
			requestsRepository.findByTmdbId(tmdbResults.results.map { result -> result.id }).bind()
		return@either tmdbResults.results.mapNotNull { result ->
			when (result) {
				is MultiSearchEntity.PersonResult -> null
				is MultiSearchEntity.TVResult -> transformTV(result, matchingRequests[result.id])
				is MultiSearchEntity.MovieResult -> transformMovie(result, matchingRequests[result.id])
			}
		}
	}

	override suspend fun searchMovie(searchTerm: String): Either<DomainError, List<SearchResult.MovieResult>> = either {
		val tmdbResults = tmdbRepository.searchMovies(searchTerm).bind()
		val matchedRequests =
			requestsRepository.findByTmdbId(tmdbResults.results.map { result -> result.id }).bind()
		return@either tmdbResults.results.map { tmdbMovie -> transformMovie(tmdbMovie, matchedRequests[tmdbMovie.id]) }
	}

	override suspend fun searchTV(searchTerm: String): Either<DomainError, List<SearchResult.TVResult>> = either {
		val tmdbResults = tmdbRepository.searchTVShows(searchTerm).bind()
		val matchedRequests =
			requestsRepository.findByTmdbId(tmdbResults.results.map { result -> result.id }).bind()
		return@either tmdbResults.results.map { tmdbTv -> transformTV(tmdbTv, matchedRequests[tmdbTv.id]) }
	}


	private fun transformTV(result: BaseTVShowEntity, request: RequestByTmdbIDData?): SearchResult.TVResult =
		SearchResult.TVResult(
			id = result.id,
			overview = result.overview ?: "",
			posterPath = ImageTools.tmdbPosterPath(result.posterPath),
			releaseDate = result.firstAirDate,
			title = result.title,
			request = request?.let { SearchResult.RequestData(request.id, request.status) }
		)

	private fun transformMovie(result: BaseMovieEntity, request: RequestByTmdbIDData?): SearchResult.MovieResult =
		SearchResult.MovieResult(
			id = result.id,
			overview = result.overview ?: "",
			posterPath = ImageTools.tmdbPosterPath(result.posterPath),
			title = result.title,
			releaseDate = result.releaseDate,
			request = request?.let { SearchResult.RequestData(request.id, request.status) }
		)
}