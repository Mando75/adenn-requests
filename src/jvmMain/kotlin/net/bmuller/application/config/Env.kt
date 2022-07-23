package net.bmuller.application.config

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

private const val PORT: Int = 8080
private const val JDBC_USER: String = "adenn_user"
private const val JDBC_PASSWORD: String = "adenn123"
private const val JDBC_URL: String = "jdbc:postgresql://localhost:5555/adenn_requests"
private const val JDBC_DRIVER: String = "org.postgresql.Driver"
private const val SESSION_SECRET_ENCRYPT_KEY: String = "session-secret-encrypt-key"
private const val SESSION_SIGN_KEY: String = "session-sign-key"
private const val JWT_TOKEN_SECRET: String = "jwt-token-secret"
private const val JWT_REALM: String = "api"
private const val JWT_ISSUER: String = "adenn-requests"
private const val JWT_AUDIENCE: String = "localhost"
private val JWT_LIFETIME: Int = 14.days.toInt(DurationUnit.MILLISECONDS)
private const val MOVIE_DB_HOST = "api.themoviedb.org"
private const val MOVIE_DB_API_KEY = "my-moviedb-api-key"
private const val MOVIE_DB_REQUEST_TOKEN = "my-moviedb-request-token"
private const val MOVIE_DB_SESSION_TOKEN = "my-moviedb-session-token"
private const val PLEX_MACHINE_ID = "my-plex-machine-id"
private const val PLEX_HOST = "plex.tv"
private const val PLEX_AUTH_HOST = "app.plex.tv"
private const val PLEX_AUTH_PATH = "auth"
private const val PLEX_CLIENT_ID = "7909d93e-0876-42c8-99a8-ea5c1c3c3bd5"
private const val PLEX_PRODUCT = "Adenn Requests"
private const val PLEX_DEVICE = "Web"
private const val PLEX_VERSION = "0.0.1"
private const val PLEX_PLATFORM = "Web"

private val getenv: (key: String) -> String? = { key -> dotenv()[key] }

enum class RunEnv {
	TESTING,
	DEVELOPMENT,
	PRODUCTION;

	companion object {
		fun getRunEnv() = when (getenv("RUN_ENV")) {
			"TESTING" -> TESTING
			"PRODUCTION" -> PRODUCTION
			else -> DEVELOPMENT
		}
	}
}

data class Env(
	val dataSource: DataSource = DataSource(),
	val http: Http = Http(),
	val auth: Auth = Auth(),
	val tmdb: TMDB = TMDB(),
	val plex: Plex = Plex(),
	val runEnv: RunEnv = RunEnv.getRunEnv()
) {
	data class Http(
		val host: String = getenv("HOST") ?: "0.0.0.0",
		val port: Int = getenv("PORT")?.toIntOrNull() ?: PORT,
		val prod: Boolean = getenv("PROD")?.toBooleanStrictOrNull() ?: false,
		val clientEngine: HttpClientEngine = CIO.create()
	)

	data class DataSource(
		val url: String = getenv("POSTGRES_URL") ?: JDBC_URL,
		val username: String = getenv("POSTGRES_USER") ?: JDBC_USER,
		val password: String = getenv("POSTGRES_PASSWORD") ?: JDBC_PASSWORD,
		val driver: String = JDBC_DRIVER
	)

	data class Auth(
		val sessionSecretEncryptKey: String = getenv("SESSION_SECRET_ENCRYPT_KEY") ?: SESSION_SECRET_ENCRYPT_KEY,
		val sessionSignKey: String = getenv("SESSION_SIGN_KEY") ?: SESSION_SIGN_KEY,
		val jwtTokenSecret: String = getenv("JWT_TOKEN_SECRET") ?: JWT_TOKEN_SECRET,
		val jwtRealm: String = getenv("JWT_REALM") ?: JWT_REALM,
		val jwtIssuer: String = getenv("JWT_ISSUER") ?: JWT_ISSUER,
		val jwtAudience: String = getenv("JWT_AUDIENCE") ?: JWT_AUDIENCE,
		val jwtTokenLifetime: Int = getenv("JWT_TOKEN_LIFETIME")?.toIntOrNull() ?: JWT_LIFETIME
	)

	data class TMDB(
		val host: String = getenv("MOVIE_DB_HOST") ?: MOVIE_DB_HOST,
		val apiKey: String = getenv("MOVIE_DB_API_KEY") ?: MOVIE_DB_API_KEY,
		val requestToken: String = getenv("MOVIE_DB_REQUEST_TOKEN") ?: MOVIE_DB_REQUEST_TOKEN,
		val sessionToken: String = getenv("MOVIE_DB_SESSION_TOKEN") ?: MOVIE_DB_SESSION_TOKEN,
	)

	data class Plex(
		val host: String = getenv("PLEX_HOST") ?: PLEX_HOST,
		val machineId: String = getenv("PLEX_MACHINE_ID") ?: PLEX_MACHINE_ID,
		val authHost: String = getenv("PLEX_AUTH_HOST") ?: PLEX_AUTH_HOST,
		val authPath: String = getenv("PLEX_AUTH_PATH") ?: PLEX_AUTH_PATH,
		val clientId: String = getenv("PLEX_CLIENT_ID") ?: PLEX_CLIENT_ID,
		val product: String = getenv("PLEX_PRODUCT") ?: PLEX_PRODUCT,
		val device: String = getenv("PLEX_DEVICE") ?: PLEX_DEVICE,
		val version: String = getenv("PLEX_VERSION") ?: PLEX_VERSION,
		val platform: String = getenv("PLEX_PLATFORM") ?: PLEX_PLATFORM
	)

}