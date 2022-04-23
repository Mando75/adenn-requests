package net.bmuller.application.di

import db.ExposedDatabase
import org.koin.dsl.module

val databaseModule = module {
	single { ExposedDatabase() }
}