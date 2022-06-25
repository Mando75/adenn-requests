package hooks

import react.StateSetter
import react.useState

data class UsePagination(val page: Int, val queryPage: Long, val setPage: StateSetter<Int>)

fun usePagination(): UsePagination {
	val (queryPage, setPage) = useState(0)
	val page = queryPage + 1

	return UsePagination(page, queryPage.toLong(), setPage)
}