package db.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

@Suppress("unused")
object UserTable : IntIdTable("users") {
	val externalId: Column<String> = text("external_id").uniqueIndex()
	val name: Column<String> = text("name")
	val email: Column<String> = text("email").uniqueIndex()
}