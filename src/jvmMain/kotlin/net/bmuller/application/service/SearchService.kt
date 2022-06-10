package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.effect
import entities.SearchResult
import net.bmuller.application.entities.BaseMovieResult
import net.bmuller.application.entities.BaseTVShowResult
import net.bmuller.application.entities.MultiSearchEntity

class SearchService : BaseService() {

	suspend fun searchMulti(searchTerm: String): Either<Throwable, List<SearchResult>> = effect<Throwable, List<SearchResult>> {
		val tmdbResults = tmdbRepository.searchMulti(searchTerm).bind()
		return@effect tmdbResults.results.mapNotNull { result ->
			when (result) {
				is MultiSearchEntity.PersonResult -> null
				is MultiSearchEntity.TVResult -> transformTV(result)
				is MultiSearchEntity.MovieResult -> transformMovie(result)
			}
		}
	}.toEither()

	suspend fun searchMovie(searchTerm: String): Either<Throwable, List<SearchResult.MovieResult>> = effect<Throwable, List<SearchResult.MovieResult>> {
		val tmdbResults = tmdbRepository.searchMovies(searchTerm).bind()
		return@effect tmdbResults.results.map { tmdbMovie -> transformMovie(tmdbMovie) }
	}.toEither()

	suspend fun searchTV(searchTerm: String): Either<Throwable, List<SearchResult.TVResult>> = effect<Throwable, List<SearchResult.TVResult>> {
		val tmdbResults = tmdbRepository.searchTVShows(searchTerm).bind()
		return@effect tmdbResults.results.map { tmdbTv -> transformTV(tmdbTv) }
	}.toEither()


	private fun transformTV(result: BaseTVShowResult): SearchResult.TVResult = SearchResult.TVResult(
		id = result.id,
		overview = result.overview,
		posterPath = result.posterPath,
		releaseDate = result.firstAirDate,
		title = result.title,
	)

	private fun transformMovie(result: BaseMovieResult): SearchResult.MovieResult =
		SearchResult.MovieResult(
			id = result.id,
			overview = result.overview,
			posterPath = result.posterPath,
			title = result.title,
			releaseDate = result.releaseDate
		)
}

