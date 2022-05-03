package db

import db.util.dataSource
import net.bmuller.application.config.EnvironmentValues
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

class ExposedDatabase {
	private val env by inject<EnvironmentValues>(EnvironmentValues::class.java)

	fun createDatabase(): Database {
		val host = env.postgresHost
		val port = env.postgresPort
		val database = env.postgresDatabase
		val user = env.postgresUser
		val password = env.postgresPassword

		val url = "jdbc:postgresql://$host:$port/$database"
		val cleanDB = false

		return Database.connect(
			dataSource(url, user, password)
		).apply {
			migrate(cleanDB, url, user, password)
		}
	}
}