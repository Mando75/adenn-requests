import db.util.hikari
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
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

	private val plex: Env.Plex by lazy {
		Env.Plex(machineId = "my-plex-machine-id")
	}

	private val http: Env.Http by lazy {
		Env.Http(clientEngine = mockEngine)
	}

	private val env: Env by lazy {
		Env().copy(
			dataSource = dataSource,
			http = http,
			plex = plex,
			runEnv = RunEnv.TESTING
		)
	}

	val dependencies = TestResource { dependencies(env) }
	private val hikari = TestResource { hikari(env.dataSource) }

	private val resetDatabaseListener = object : TestListener {
		override suspend fun afterTest(testCase: TestCase, result: TestResult) {
			super.afterTest(testCase, result)
			val ds by hikari
			ds.connection.use { conn ->
				conn.prepareStatement("TRUNCATE users CASCADE").executeLargeUpdate()
			}
		}
	}

	private val insertAdminUserListener = object : TestListener {
		override suspend fun beforeTest(testCase: TestCase) {
			super.beforeTest(testCase)
			val ds by hikari
			ds.connection.use { conn ->
				conn.prepareStatement(
					"""
						INSERT INTO users (plex_username, plex_id, plex_token, email, user_type)
						VALUES ('test-admin', 14891, 'test-admin-token', 'testadmin@bmuller.net', 'ADMIN')
						ON CONFLICT (plex_username) DO NOTHING
				
				""".trimIndent()
				).execute()
			}
		}
	}

	override fun extensions(): List<Extension> =
		listOf(postgres, hikari, dependencies, insertAdminUserListener, resetDatabaseListener)
}