package db.tables

import db.util.postgresEnumeration
import entities.UserEntity
import entities.UserType
import net.bmuller.application.entities.AdminUser
import net.bmuller.application.lib.toKotlinInstant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserTable : IntIdTable("users") {
	// User metadata
	val plexUsername: Column<String> = text("plex_username").uniqueIndex()
	val plexId: Column<Int> = integer("plex_id").uniqueIndex()
	val plexToken: Column<String> = text("plex_token").uniqueIndex()
	val plexProfilePicUrl: Column<String?> = text("plex_profile_picture_url").nullable()
	val email: Column<String> = text("email").uniqueIndex()
	val userType: Column<UserType> = postgresEnumeration("user_type", "User_Type_Enum")

	// User request data
	val requestCount: Column<Int> = integer("request_count").default(0)
	val movieQuotaLimit: Column<Int> = integer("movie_quota_limit").default(5)
	val movieQuotaDays: Column<Int> = integer("movie_quota_days").default(1)
	val tvQuotaLimit: Column<Int> = integer("tv_quota_limit").default(5)
	val tvQuotaDays: Column<Int> = integer("tv_quota_days").default(1)

	// Table meta
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())
	val authVersion: Column<Int> = integer("auth_version").default(0)
}

fun ResultRow.toUserEntity(): UserEntity {
	return UserEntity(
		id = get(UserTable.id).value,
		plexUsername = get(UserTable.plexUsername),
		plexId = get(UserTable.plexId),
		profilePicUrl = get(UserTable.plexProfilePicUrl),
		email = get(UserTable.email),
		userType = get(UserTable.userType),
		requestCount = get(UserTable.requestCount),
		movieQuotaLimit = get(UserTable.movieQuotaLimit),
		movieQuotaDays = get(UserTable.movieQuotaDays),
		tvQuotaLimit = get(UserTable.tvQuotaLimit),
		tvQuotaDays = get(UserTable.tvQuotaDays),
		createdAt = get(UserTable.createdAt).toKotlinInstant(),
		modifiedAt = get(UserTable.modifiedAt).toKotlinInstant(),
		authVersion = get(UserTable.authVersion)
	)
}

fun ResultRow.toAdminUser(): AdminUser {
	return AdminUser(
		id = get(UserTable.id).value,
		plexUsername = get(UserTable.plexUsername),
		plexId = get(UserTable.plexId),
		plexToken = get(UserTable.plexToken),
		profilePicUrl = get(UserTable.plexProfilePicUrl),
		email = get(UserTable.email),
		userType = get(UserTable.userType),
		requestCount = get(UserTable.requestCount),
		movieQuotaLimit = get(UserTable.movieQuotaLimit),
		movieQuotaDays = get(UserTable.movieQuotaDays),
		tvQuotaLimit = get(UserTable.tvQuotaLimit),
		tvQuotaDays = get(UserTable.tvQuotaDays),
		createdAt = get(UserTable.createdAt).toKotlinInstant(),
		modifiedAt = get(UserTable.modifiedAt).toKotlinInstant()
	)
}