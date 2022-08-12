package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import entities.MediaType
import entities.Provider
import entities.tmdb.DetailEntity
import entities.tmdb.unwrapToProviders
import net.bmuller.application.config.Env
import net.bmuller.application.lib.DomainError
import net.bmuller.application.repository.TMDBRepository

interface MediaService {
	suspend fun getMediaDetail(id: Int, type: MediaType): Either<DomainError, DetailEntity>

	suspend fun getMediaProviders(id: Int, type: MediaType): Either<DomainError, List<Provider>>
}

fun mediaService(tmdbRepository: TMDBRepository, env: Env.TMDB): MediaService = object : MediaService {
	// TODO: Perform some transform on this to get only what we want
	override suspend fun getMediaDetail(id: Int, type: MediaType): Either<DomainError, DetailEntity> =
		when (type) {
			MediaType.MOVIE -> tmdbRepository.movieDetail(id = id)
			MediaType.TV -> tmdbRepository.tvDetail(id = id)
		}

	override suspend fun getMediaProviders(id: Int, type: MediaType): Either<DomainError, List<Provider>> = either {
		val allowedProviders = when(type) {
			MediaType.MOVIE -> env.movieProviders
			MediaType.TV -> env.tvProviders
		}
		val watchProviders = when(type) {
			MediaType.MOVIE -> tmdbRepository.movieProviders(id = id)
			MediaType.TV -> tmdbRepository.tvProviders(id = id)
		}.bind()

		watchProviders.unwrapToProviders(allowedProviders)
	}

}