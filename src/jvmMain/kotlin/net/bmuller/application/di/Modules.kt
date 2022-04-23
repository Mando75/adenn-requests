package net.bmuller.application.di

import config.ConfigProvider
import db.ExposedDatabase
import org.koin.dsl.module

val configModule = module {
	single { ConfigProvider() }
}

val databaseModule = module {
	single { ExposedDatabase().createDatabase() }
}