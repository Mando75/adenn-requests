package net.bmuller.application.config

import io.github.cdimascio.dotenv.dotenv


class EnvironmentValues {
	private val env = dotenv()

	val prod: Boolean = env["PROD"].toBoolean()
	val plexMachineId: String = env["PLEX_MACHINE_ID"]

	// Database
	val postgresUser: String = env["POSTGRES_USER"]
	val postgresPassword: String = env["POSTGRES_PASSWORD"]
	val postgresDatabase: String = env["POSTGRES_DATABASE"]
	val postgresHost: String = env["POSTGRES_HOST"]
	val postgresPort: String = env["POSTGRES_PORT"]

	// MOVIE DB
	val tmdbApiKey: String = env["MOVIE_DB_API_KEY"]
	val tmdbRequestToken: String = env["MOVIE_DB_REQUEST_TOKEN"]
	val tmdbSessionToken: String = env["MOVIE_DB_SESSION_TOKEN"]

	// AUTHENTICATION
	val sessionSecretEncryptKey: String = env["SESSION_SECRET_ENCRYPT_KEY"]
	val sessionSignKey: String = env["SESSION_SIGN_KEY"]
	val jwtTokenSecret: String = env["JWT_TOKEN_SECRET"]
	val jwtRealm: String = env["JWT_REALM"]
	val jwtIssuer: String = env["JWT_ISSUER"]
	val jwtAudience: String = env["JWT_AUDIENCE"]
}