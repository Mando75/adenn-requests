package support.extensions

class JsTriple<out A, out B, out C>
private constructor() {
	operator fun component1(): A = asDynamic()[0] as A
	operator fun component2(): B = asDynamic()[1] as B
	operator fun component3(): C = asDynamic()[2] as C
}

fun <A, B, C> JsTriple(a: A, b: B, c: C): JsTriple<A, B, C> = arrayOf(a, b, c).unsafeCast<JsTriple<A, B, C>>()

