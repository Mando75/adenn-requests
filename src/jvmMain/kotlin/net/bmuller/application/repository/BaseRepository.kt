package net.bmuller.application.repository

import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository {
	private val db: Database by inject(Database::class.java)
}