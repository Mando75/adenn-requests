package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import entities.SearchResult
import net.bmuller.application.entities.BaseMovieResult
import net.bmuller.application.entities.BaseTVShowResult
import net.bmuller.application.entities.MultiSearchEntity

class SearchService : BaseService() {

	suspend fun searchMulti(searchTerm: String): Either<Throwable, List<SearchResult>> = either {
		val tmdbResults = tmdbRepository.searchMulti(searchTerm).bind()
		return@either tmdbResults.results.mapNotNull { result ->
			when (result) {
				is MultiSearchEntity.PersonResult -> null
				is MultiSearchEntity.TVResult -> transformTV(result)
				is MultiSearchEntity.MovieResult -> transformMovie(result)
			}
		}
	}

	suspend fun searchMovie(searchTerm: String): Either<Throwable, List<SearchResult.MovieResult>> = either {
		val tmdbResults = tmdbRepository.searchMovies(searchTerm).bind()
		return@either tmdbResults.results.map { tmdbMovie -> transformMovie(tmdbMovie) }
	}

	suspend fun searchTV(searchTerm: String): Either<Throwable, List<SearchResult.TVResult>> = either {
		val tmdbResults = tmdbRepository.searchTVShows(searchTerm).bind()
		return@either tmdbResults.results.map { tmdbTv -> transformTV(tmdbTv) }
	}


	private fun transformTV(result: BaseTVShowResult): SearchResult.TVResult = SearchResult.TVResult(
		id = result.id,
		overview = result.overview,
		posterPath = createPosterPath(result.posterPath),
		releaseDate = result.firstAirDate,
		title = result.title,
	)

	private fun transformMovie(result: BaseMovieResult): SearchResult.MovieResult =
		SearchResult.MovieResult(
			id = result.id,
			overview = result.overview,
			posterPath = createPosterPath(result.posterPath),
			title = result.title,
			releaseDate = result.releaseDate
		)

	private fun createPosterPath(path: String?): String =
		path?.let { "https://image.tmdb.org/t/p/w300_and_h450_face$path" } ?: ""
}

