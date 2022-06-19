package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import entities.RequestEntity
import entities.SearchResult
import lib.PlaceholderImageUrl
import net.bmuller.application.entities.BaseMovieResult
import net.bmuller.application.entities.BaseTVShowResult
import net.bmuller.application.entities.MultiSearchEntity

class SearchService : BaseService() {

	suspend fun searchMulti(searchTerm: String): Either<Throwable, List<SearchResult>> = either {
		val tmdbResults = tmdbRepository.searchMulti(searchTerm).bind()
		val matchingRequests = requestsRepository.getRequests(tmdbResults.results.map { result -> result.id })
		return@either tmdbResults.results.mapNotNull { result ->
			when (result) {
				is MultiSearchEntity.PersonResult -> null
				is MultiSearchEntity.TVResult -> transformTV(result, matchingRequests[result.id])
				is MultiSearchEntity.MovieResult -> transformMovie(result, matchingRequests[result.id])
			}
		}
	}

	suspend fun searchMovie(searchTerm: String): Either<Throwable, List<SearchResult.MovieResult>> = either {
		val tmdbResults = tmdbRepository.searchMovies(searchTerm).bind()
		val matchedRequests = requestsRepository.getRequests(tmdbResults.results.map { result -> result.id })
		return@either tmdbResults.results.map { tmdbMovie -> transformMovie(tmdbMovie, matchedRequests[tmdbMovie.id]) }
	}

	suspend fun searchTV(searchTerm: String): Either<Throwable, List<SearchResult.TVResult>> = either {
		val tmdbResults = tmdbRepository.searchTVShows(searchTerm).bind()
		val matchedRequests = requestsRepository.getRequests(tmdbResults.results.map { result -> result.id })
		return@either tmdbResults.results.map { tmdbTv -> transformTV(tmdbTv, matchedRequests[tmdbTv.id]) }
	}


	private fun transformTV(result: BaseTVShowResult, matchedRequest: RequestEntity?): SearchResult.TVResult =
		SearchResult.TVResult(
			id = result.id,
			overview = result.overview,
			posterPath = createPosterPath(result.posterPath),
			releaseDate = result.firstAirDate,
			title = result.title,
			request = matchedRequest
		)

	private fun transformMovie(result: BaseMovieResult, matchedRequest: RequestEntity?): SearchResult.MovieResult =
		SearchResult.MovieResult(
			id = result.id,
			overview = result.overview,
			posterPath = createPosterPath(result.posterPath),
			title = result.title,
			releaseDate = result.releaseDate,
			request = matchedRequest
		)

	private fun createPosterPath(path: String?): String =
		path?.let { "https://image.tmdb.org/t/p/w300_and_h450_face$path" }
			?: PlaceholderImageUrl
}

