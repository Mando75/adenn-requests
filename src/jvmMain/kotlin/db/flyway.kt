package db

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import net.bmuller.application.config.Env
import org.flywaydb.core.Flyway

fun flyway(env: Env.DataSource): Resource<Flyway> = resource {
	Flyway.configure().dataSource(env.url, env.username, env.password).load()
}
