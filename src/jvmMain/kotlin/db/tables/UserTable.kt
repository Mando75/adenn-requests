package db.tables

import db.util.postgresEnumeration
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

@Suppress("unused")
object UserTable : IntIdTable("users") {

	enum class UserType {
		ADMIN,
		DEFAULT
	}

	val plexUsername: Column<String> = text("plex_username").uniqueIndex()
	val plexId: Column<String> = text("plex_id").uniqueIndex()
	val plexToken: Column<String> = text("plex_token").uniqueIndex()
	val email: Column<String> = text("email").uniqueIndex()
	val userType: Column<UserType> = postgresEnumeration("user_type", "User_Type_Enum")

	val requestCount: Column<Int> = integer("request_count").default(0)
	val movieQuotaLimit: Column<Int> = integer("movie_quota_limit").default(5)
	val movieQuotaDays: Column<Int> = integer("movie_quota_days").default(1)
	val tvQuotaLimit: Column<Int> = integer("tv_quota_limit").default(5)
	val tvQuotaDays: Column<Int> = integer("tv_quota_days").default(1)

	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())
}