@file:Suppress("NOTHING_TO_INLINE")

package utils

interface IJsTriple<out A, out B, out C>

class JsTriple<out A, out B, out C> private constructor() : IJsTriple<A, B, C> {
	inline operator fun component1() = asDynamic()[0] as A
	inline operator fun component2() = asDynamic()[1] as B
	inline operator fun component3() = asDynamic()[2] as C
}

inline fun <A, B, C> jsTriple(a: A, b: B, c: C): JsTriple<A, B, C> =
	arrayOf(a, b, c).unsafeCast<JsTriple<A, B, C>>()

