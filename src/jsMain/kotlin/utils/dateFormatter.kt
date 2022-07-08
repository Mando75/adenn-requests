package utils

import kotlinx.datetime.Instant
import kotlinx.datetime.toJSDate
import kotlinx.js.jso

fun dateFormatter(inst: Instant): String = inst.toJSDate().toLocaleDateString("en-us", jso {
	year = "numeric"
	month = "short"
	day = "numeric"
})