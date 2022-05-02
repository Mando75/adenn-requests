package net.bmuller.application.repository

import net.bmuller.application.http.PlexClient
import net.bmuller.application.http.TMDBClient
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository {
	protected val db: Database by inject(Database::class.java)
	protected val tmdb: TMDBClient by inject(TMDBClient::class.java)
	protected val plex: PlexClient by inject(PlexClient::class.java)
}