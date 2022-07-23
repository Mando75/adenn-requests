package db

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource


fun exposed(hikari: DataSource, flyway: Flyway, cleanDB: Boolean = false): Resource<Database> = resource {
	Database.connect(hikari).apply {
		if (cleanDB) {
			flyway.clean()
		}
		flyway.migrate()
	}
}
