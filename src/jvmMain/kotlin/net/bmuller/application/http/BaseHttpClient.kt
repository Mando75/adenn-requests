package net.bmuller.application.http

import arrow.core.Either
import config.ConfigProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject

abstract class BaseHttpClient {
	val configProvider: ConfigProvider by inject(ConfigProvider::class.java)

	private val defaultJsonBuilder = Json {
		encodeDefaults = true
		ignoreUnknownKeys = true
		prettyPrint = true
	}

	private fun createClient(jsonBuilder: Json = defaultJsonBuilder) = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(jsonBuilder)
		}
	}

	private suspend inline fun <reified TReturnType> makeRequest(
		builder: HttpRequestBuilder,
		client: HttpClient = createClient()
	) = Either.catch {
		val response = client.request(builder)
		val body: TReturnType = response.body()
		client.close()
		return@catch Pair(body, response)
	}
}