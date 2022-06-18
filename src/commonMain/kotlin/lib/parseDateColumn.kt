package lib

import kotlinx.datetime.Instant

fun parseDateColumn(column: Long) = Instant.fromEpochSeconds(column)
fun parseNullableDateColumn(column: Long?) = column?.let { date -> parseDateColumn(date) }