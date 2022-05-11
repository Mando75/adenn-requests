package support

import entities.UserEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val apiClient = HttpClient {
	expectSuccess = true
	install(Resources)
	install(ContentNegotiation) {
		json(Json {
			encodeDefaults = true
			ignoreUnknownKeys = true
			prettyPrint = true
		})
	}
	install(DefaultRequest) {
		url("/api/v1")
	}
}

@Resource("/v1/users")
@kotlinx.serialization.Serializable
class UserResource {
	@Resource("me")
	@kotlinx.serialization.Serializable
	class Me(@Suppress("unused") val parent: UserResource = UserResource())
}

suspend fun getMe(): UserEntity = apiClient.get(resource = UserResource.Me()).body()
