package net.bmuller.application.config

import io.github.cdimascio.dotenv.dotenv


class EnvironmentValues {
	private val env = dotenv()

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

}