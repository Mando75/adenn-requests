package net.bmuller.application.db.postgres.util

import net.bmuller.application.db.postgres.DbSettings
import net.bmuller.application.db.postgres.tables.MediaItemTable
import net.bmuller.application.db.postgres.tables.RequestTable
import net.bmuller.application.db.postgres.tables.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun createDb() {
	transaction(DbSettings.db) {
		addLogger(StdOutSqlLogger)
		MediaItemTable.ENUM_TYPES.entries.forEach { entry -> exec(createEnum(entry.key, entry.value)) }
		RequestTable.ENUM_TYPES.entries.forEach { entry -> exec(createEnum(entry.key, entry.value)) }
		SchemaUtils.createMissingTablesAndColumns(UserTable, MediaItemTable, RequestTable)
	}
}

fun createEnum(enumName: String, enumValues: List<String>): String {
	val joinedValues = enumValues.joinToString(",") { value -> "'$value'" }
	return """
		DO $$ BEGIN
		  CREATE TYPE $enumName AS ENUM($joinedValues);
		EXCEPTION
		  WHEN duplicate_object THEN null;
		END $$;
	""".trimIndent()
}

