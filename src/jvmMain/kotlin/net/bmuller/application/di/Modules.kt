package net.bmuller.application.di

import db.ExposedDatabase
import net.bmuller.application.config.ConfigProvider
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

val tmdbModule = module {
	single { TMDBClient() }
}

val repoModule = module {
	single<TMDBRepository> { TMDBRepositoryImpl() }
}