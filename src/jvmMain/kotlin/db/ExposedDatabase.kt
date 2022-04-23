package db

import config.ConfigProvider
import config.IConfigProvider
import org.jetbrains.exposed.sql.Database

class ExposedDatabase(private val config: IConfigProvider = ConfigProvider()) {
	private val hostKey = "POSTGRES_HOST"
	private val userKey = "POSTGRES_USER"
	private val passwordKey = "POSTGRES_PASSWORD"
	private val portKey = "POSTGRES_PORT"
	private val dbKey = "POSTGRES_DATABASE"

	fun createDatabase(): Database {
		val host = config.getOptionalValue(hostKey)
		val port = config.getOptionalValue(portKey)
		val database = config.getOptionalValue(dbKey)
		val user = config.getOptionalValue(userKey) ?: "postgres"
		val password = config.getOptionalValue(passwordKey) ?: ""

		val url = "jdbc:postgresql://$host:$port/$database"
		val cleanDB = false

		return Database.connect(
			dataSource(url, user, password)
		).apply {
			migrate(cleanDB, url, user, password)
		}
	}
}