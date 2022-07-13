package net.bmuller.application.config

import io.github.cdimascio.dotenv.dotenv
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
private const val JWT_REALM: String = "api/v1"
private const val JWT_ISSUER: String = "adenn-requests"
private const val JWT_AUDIENCE: String = "localhost"
private val JWT_LIFETIME: Int = 14.days.toInt(DurationUnit.MILLISECONDS)
private const val MOVIE_DB_HOST = "api.themoviedb.org"
private const val MOVIE_DB_API_KEY = "my-moviedb-api-key"
private const val MOVIE_DB_REQUEST_TOKEN = "my-moviedb-request-token"
private const val MOVIE_DB_SESSION_TOKEN = "my-moviedb-session-token"
private const val PLEX_MACHINE_ID = "my-plex-machine-id"

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
		val prod: Boolean = getenv("PROD")?.toBooleanStrictOrNull() ?: false
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
		val machineId: String = getenv("PLEX_MACHINE_ID") ?: PLEX_MACHINE_ID
	)

}