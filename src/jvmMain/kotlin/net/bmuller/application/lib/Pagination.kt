package net.bmuller.application.lib

import entities.Pagination
import kotlin.math.ceil
import kotlin.math.roundToLong

private const val PAGE_SIZE: Int = 25

fun calcOffset(pageNumber: Long, pageSize: Int = PAGE_SIZE): Pagination {
	val offset = pageNumber * pageSize
	return Pagination(offset = offset, limit = pageSize)
}

fun calcTotalPages(totalCount: Long, pageSize: Int = PAGE_SIZE): Long {
	return ceil(totalCount.div(pageSize.toDouble())).roundToLong()
}
