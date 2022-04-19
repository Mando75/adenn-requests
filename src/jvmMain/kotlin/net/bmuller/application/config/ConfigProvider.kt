package net.bmuller.application.config

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.github.cdimascio.dotenv.dotenv

val env = dotenv()

sealed class MissingEnvException {
	object NullKey : MissingEnvException()
}

interface IConfigProvider {
	fun getValue(key: String): Either<MissingEnvException, String>
	fun getOptionalValue(key: String): String?
}

class ConfigProvider : IConfigProvider {
	override fun getValue(key: String): Either<MissingEnvException, String> =
		env[key]?.let(::Right) ?: Left(MissingEnvException.NullKey)

	override fun getOptionalValue(key: String): String? {
		return env[key]
	}

}