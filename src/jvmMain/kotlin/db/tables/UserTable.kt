package db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

@Suppress("unused")
object UserTable : IntIdTable("users") {
	val externalId: Column<String> = text("external_id").uniqueIndex()
	val name: Column<String> = text("name")
	val email: Column<String> = text("email").uniqueIndex()
	val createdAt: Column<Instant> = timestamp("created_at").index().default(Instant.now())
	val modifiedAt: Column<Instant> = timestamp("modified_at").index().default(Instant.now())
}