package net.bmuller.application.repository

import net.bmuller.application.config.ConfigProvider
import net.bmuller.application.http.tmdb.TMDBClient
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository {
	protected val db: Database by inject(Database::class.java)
	protected val tmdbClient: TMDBClient by inject(TMDBClient::class.java)
	protected val configProvider: ConfigProvider by inject(ConfigProvider::class.java)
}