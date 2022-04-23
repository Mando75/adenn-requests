package net.bmuller.application.entities

import db.tables.MediaItemTable
import java.time.LocalDate

@Suppress("unused")
data class MediaItem(
	val id: Int,
	val mediaType: MediaItemTable.MediaItemType,
	val overview: String,
	val releaseDate: LocalDate,
	val title: String,
	val posterUrl: String?,
	val backgroundUrl: String?,
	val tmdbUrl: String,
	val popularity: Float,
	val tmdbId: Int
)
