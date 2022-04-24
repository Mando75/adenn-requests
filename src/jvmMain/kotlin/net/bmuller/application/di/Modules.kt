package net.bmuller.application.di

import db.ExposedDatabase
import net.bmuller.application.config.ConfigProvider
import net.bmuller.application.http.plex.PlexClient
import net.bmuller.application.http.tmdb.TMDBClient
import net.bmuller.application.repository.TMDBRepository
import net.bmuller.application.repository.TMDBRepositoryImpl
import org.koin.dsl.module

val configModule = module {
	single { ConfigProvider() }
}

val databaseModule = module {
	single { ExposedDatabase().createDatabase() }
}

val httpModule = module {
	single { TMDBClient() }
	single { PlexClient() }
}

val repoModule = module {
	single<TMDBRepository> { TMDBRepositoryImpl() }
}