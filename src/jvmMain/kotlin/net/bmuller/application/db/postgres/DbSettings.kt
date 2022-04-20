package net.bmuller.application.db.postgres

import net.bmuller.application.config.ConfigProvider
import org.jetbrains.exposed.sql.Database

object DbSettings {
	private const val hostKey = "POSTGRES_HOST"
	private const val userKey = "POSTGRES_USER"
	private const val passwordKey = "POSTGRES_PASSWORD"
	private const val portKey = "POSTGRES_PORT"
	private const val dbKey = "POSTGRES_DATABASE"
	private val config = ConfigProvider()

	val db by lazy {
		val host = config.getOptionalValue(hostKey)
		val port = config.getOptionalValue(portKey)
		val database = config.getOptionalValue(dbKey)
		val user = config.getOptionalValue(userKey) ?: "postgres"
		val password = config.getOptionalValue(passwordKey) ?: ""

		val url = "jdbc:postgresql://$host:$port/$database"

		Database.connect(
			url,
			driver = "org.postgresql.Driver",
			user = user,
			password = password
		)
	}
}