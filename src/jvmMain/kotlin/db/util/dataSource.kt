package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun dataSource(jdbcUrl: String, username: String, password: String): DataSource {
	val config = HikariConfig().apply {
		this.jdbcUrl = jdbcUrl
		this.username = username
		this.password = password
		isAutoCommit = true
		transactionIsolation = "TRANSACTION_REPEATABLE_READ"
	}

	return HikariDataSource(config)
}