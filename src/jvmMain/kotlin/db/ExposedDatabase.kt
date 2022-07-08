package db

import db.util.dataSource
import net.bmuller.application.config.Env
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

class ExposedDatabase {
	private val env by inject<Env>(Env::class.java)

	fun createDatabase(): Database {
		val user = env.dataSource.username
		val password = env.dataSource.password

		val url = env.dataSource.url
		val cleanDB = false

		return Database.connect(
			dataSource(url, user, password)
		).apply {
			migrate(cleanDB, url, user, password)
		}
	}
}