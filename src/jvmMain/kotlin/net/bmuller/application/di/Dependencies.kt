package net.bmuller.application.di

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import db.exposed
import db.util.hikari
import net.bmuller.application.config.Env
import net.bmuller.application.http.plexClient
import net.bmuller.application.http.tmdbClient
import net.bmuller.application.repository.*
import net.bmuller.application.service.*

class Dependencies(
	val plexOAuthService: IPlexOAuthService,
	val userAuthService: IUserAuthService,
	val userService: IUserService,
	val searchService: ISearchService,
	val requestService: IRequestService,
)

fun dependencies(env: Env): Resource<Dependencies> = resource {
	// Database
	val hikari = hikari(env.dataSource).bind()
	val exposed = exposed(hikari, env.dataSource).bind()

	// HTTP Clients
	val plexClient = plexClient()
	val tmdbClient = tmdbClient(env.tmdb)

	// Repositories
	val plexAuthPinRepo = plexAuthPinRepository(plexClient)
	val plexTVRepo = plexTVRepository(plexClient)
	val requestsRepo = requestsRepository(exposed)
	val tmdbRepo = tmdbRepository(tmdbClient)
	val userRepo = userRepository(exposed)

	// admin user
	val adminUser = userRepo.getAdminUser()

	// Services
	val plexOAuthService = plexOAuthService(plexAuthPinRepo)
	val requestsService = requestService(requestsRepo, tmdbRepo, userRepo)
	val searchService = searchService(tmdbRepo, requestsRepo)
	val userAuthService = userAuthService(env.plex, adminUser, userRepo, plexTVRepo)
	val userService = userService(userRepo)
	Dependencies(plexOAuthService, userAuthService, userService, searchService, requestsService)
}