package net.bmuller.application.di

import db.ExposedDatabase
import net.bmuller.application.config.EnvironmentValues
import net.bmuller.application.http.plex.PlexClient
import net.bmuller.application.http.plex.PlexClientImpl
import net.bmuller.application.http.tmdb.TMDBClient
import net.bmuller.application.http.tmdb.TMDBClientImpl
import net.bmuller.application.repository.PlexAuthPinRepository
import net.bmuller.application.repository.PlexAuthPinRepositoryImpl
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.TMDBRepositoryImpl
import org.koin.dsl.module

val envModule = module {
	single { EnvironmentValues() }
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
}