package db

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import db.util.dataSource
import net.bmuller.application.config.Env
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject
import javax.sql.DataSource


fun exposed(dataSource: DataSource, env: Env.DataSource, cleanDB: Boolean = false): Resource<Database> = resource {
	Database.connect(dataSource).apply { migrate(cleanDB, env.url, env.username, env.password) }
}

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