package utils

import react.Props


fun <T> Props.setDataAttr(key: String, value: T) {
	asDynamic()["data-$key"] = value
}
