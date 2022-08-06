package lib.reactQuery

import react.query.EnsuredQueryKey
import react.query.QueryKey
import utils.JsPair
import utils.JsTriple
import utils.jsPair
import utils.jsTriple

fun <T : QueryKey> createQueryKey(vararg keys: Any): T = keys.unsafeCast<T>()

@Suppress("UNCHECKED_CAST")
fun <T> parseListQueryKey(key: EnsuredQueryKey<QueryKey>): List<T> = (key as Array<T>).toList()

fun <A, B, C> parseTripleQueryKey(key: EnsuredQueryKey<QueryKey>): JsTriple<A, B, C> {
	val list = (key as Array<*>).toList()
	return jsTriple(list[0].unsafeCast<A>(), list[1].unsafeCast<B>(), list[2].unsafeCast<C>())
}

fun <A, B> parsePairQueryKey(key: EnsuredQueryKey<QueryKey>): JsPair<A, B> {
	val list = (key as Array<*>).toList()
	return jsPair(list[0].unsafeCast<A>(), list[1].unsafeCast<B>())
}