package db

import org.jetbrains.exposed.sql.Database

class ExposedProvider(val db: Database = ExposedDatabase.db)