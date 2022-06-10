package support

import react.query.EnsuredQueryKey
import react.query.QueryKey

fun <T : QueryKey> CreateQueryKey(vararg keys: Any): T = keys.unsafeCast<T>()

@Suppress("UNCHECKED_CAST")
fun <T> ParseQueryKey(key: EnsuredQueryKey<QueryKey>): List<T> = (key as Array<T>).toList()
