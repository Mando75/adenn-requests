package net.bmuller.application.di

import db.ExposedDatabase
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.http.PlexClient
import net.bmuller.application.http.PlexClientImpl
import net.bmuller.application.http.TMDBClient
import net.bmuller.application.http.TMDBClientImpl
import net.bmuller.application.repository.*
import net.bmuller.application.service.PlexOAuthService
import net.bmuller.application.service.SearchService
import net.bmuller.application.service.UserAuthService
import net.bmuller.application.service.UserService
import org.koin.dsl.module
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val envModule = module {
	single { EnvironmentValues() }
	single<Logger> { LoggerFactory.getLogger("Primary") }
}

val databaseModule = module {
	single { ExposedDatabase().createDatabase() }
}

val httpModule = module {
	single<TMDBClient> { TMDBClientImpl() }
	single<PlexClient> { PlexClientImpl() }
}

val repoModule = module {
	single<TMDBRepository> { TMDBRepositoryImpl() }
	single<PlexAuthPinRepository> { PlexAuthPinRepositoryImpl() }
	single<PlexTVRepository> { PlexTVRepositoryImpl() }
	single<UserRepository> { UserRepositoryImpl() }
	single<RequestsRepository> { RequestsRepositoryImpl() }
}

val serviceModule = module {
	single { PlexOAuthService() }
	single { UserAuthService() }
	single { UserService() }
	single { SearchService() }
}

val adminModule = module {
	single { UserRepositoryImpl().getAdminUser() }
}