package db.util

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.fromCloseable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.bmuller.application.config.Env

fun hikari(env: Env.DataSource): Resource<HikariDataSource> = Resource.fromCloseable {
	HikariDataSource(
		HikariConfig().apply {
			jdbcUrl = env.url
			username = env.username
			password = env.password
			driverClassName = env.driver
			isAutoCommit = true
			transactionIsolation = "TRANSACTION_REPEATABLE_READ"
		}
	)
}