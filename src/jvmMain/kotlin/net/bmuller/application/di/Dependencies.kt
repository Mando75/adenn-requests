package net.bmuller.application.di

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import net.bmuller.application.config.Env
import net.bmuller.application.http.plexClient
import net.bmuller.application.http.tmdbClient
import net.bmuller.application.repository.plexAuthPinRepository
import net.bmuller.application.repository.plexTVRepository
import net.bmuller.application.service.*

class Dependencies(
	val plexOAuthService: IPlexOAuthService,
	val userAuthService: UserAuthService,
	val userService: UserService,
	val searchService: SearchService,
	val requestService: RequestService,
)

fun dependencies(env: Env): Resource<Unit> = resource {
	val plexClient = plexClient()
	val tmdbClient = tmdbClient(env.tmdb)
	val plexAuthPinRepo = plexAuthPinRepository(plexClient)
	val plexTVRepo = plexTVRepository(plexClient)

	val plexOAuthService = plexOAuthService(plexAuthPinRepo)
}