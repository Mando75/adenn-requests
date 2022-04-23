package net.bmuller.application.http

import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import net.bmuller.application.config.ConfigProvider
import org.koin.java.KoinJavaComponent.inject

abstract class BaseHttpClient {
	protected val configProvider: ConfigProvider by inject(ConfigProvider::class.java)

	private val defaultJsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
	}

	fun createClient(jsonBuilder: Json = defaultJsonBuilder) = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(jsonBuilder)
		}
	}

	suspend inline fun <reified TReturnType> makeRequest(
		builder: HttpRequestBuilder,
		client: HttpClient = createClient()
	) = Either.catch {
		withContext(Dispatchers.IO) {
			val response = client.request(builder)
			val body: TReturnType = response.body()
			client.close()
			Pair(body, response)
		}
	}
}