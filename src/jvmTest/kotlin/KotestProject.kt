import db.util.hikari
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.testcontainers.StartablePerProjectListener
import net.bmuller.application.config.Env
import net.bmuller.application.config.RunEnv
import net.bmuller.application.di.dependencies
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

private class PostgresSQL : PostgreSQLContainer<PostgresSQL>("postgres:latest") {
	init {
		waitingFor(Wait.forListeningPort())
	}
}

object KotestProject : AbstractProjectConfig() {
	private val postgres = StartablePerProjectListener(PostgresSQL(), "postgres")

	private val dataSource: Env.DataSource by lazy {
		Env.DataSource(
			postgres.startable.jdbcUrl,
			postgres.startable.username,
			postgres.startable.password,
			postgres.startable.driverClassName
		)
	}

	private val http: Env.Http by lazy {
		Env.Http(clientEngine = mockEngine)
	}

	private val env: Env by lazy { Env().copy(dataSource = dataSource, http = http, runEnv = RunEnv.TESTING) }

	val dependencies = TestResource { dependencies(env, cleanDB = true) }
	private val hikari = TestResource { hikari(env.dataSource) }

	override fun extensions(): List<Extension> = listOf(postgres, hikari, dependencies)
}