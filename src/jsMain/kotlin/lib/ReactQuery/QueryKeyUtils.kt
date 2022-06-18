package lib.ReactQuery

import react.query.EnsuredQueryKey
import react.query.QueryKey

fun <T : QueryKey> createQueryKey(vararg keys: Any): T = keys.unsafeCast<T>()

@Suppress("UNCHECKED_CAST")
fun <T> parseQueryKey(key: EnsuredQueryKey<QueryKey>): List<T> = (key as Array<T>).toList()
