package net.bmuller.application.di

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import db.exposed
import db.flyway
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
	val env: Env,
)

fun dependencies(env: Env, cleanDB: Boolean = false): Resource<Dependencies> = resource {
	// Database
	val hikari = hikari(env.dataSource).bind()
	val flyway = flyway(env.dataSource)
	val exposed = exposed(hikari, flyway, cleanDB).bind()

	// HTTP Clients
	val plexClient = plexClient(env.plex, env.http.clientEngine)
	val tmdbClient = tmdbClient(env.tmdb, env.http.clientEngine)

	// Repositories
	val plexAuthPinRepo = plexAuthPinRepository(plexClient)
	val plexTVRepo = plexTVRepository(plexClient)
	val requestsRepo = requestsRepository(exposed)
	val tmdbRepo = tmdbRepository(tmdbClient)
	val userRepo = userRepository(exposed)

	// Services
	val plexOAuthService = plexOAuthService(plexAuthPinRepo, env.plex)
	val requestsService = requestService(requestsRepo, tmdbRepo, userRepo, env)
	val searchService = searchService(tmdbRepo, requestsRepo)
	val userAuthService = userAuthService(env, userRepo, plexTVRepo)
	val userService = userService(userRepo)
	Dependencies(plexOAuthService, userAuthService, userService, searchService, requestsService, env)
}