package db

import net.bmuller.application.config.Env
import org.flywaydb.core.Flyway

fun flyway(env: Env.DataSource): Flyway =
	Flyway.configure().dataSource(env.url, env.username, env.password).load()

