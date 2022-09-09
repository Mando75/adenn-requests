package lib

fun <T> Boolean.render(block: () -> T): T? = when {
	this -> {
		block()
	}

	else -> null
}
