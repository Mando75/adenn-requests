import arrow.core.Either
import arrow.core.continuations.either
import entities.AuthTokenResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import lib.JsonSchemaDiscriminator
import net.bmuller.application.di.Dependencies
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.DomainError
import net.bmuller.application.plugins.configureAuthentication
import net.bmuller.application.plugins.configureContentNegotiation
import net.bmuller.application.plugins.configureLogging
import net.bmuller.application.plugins.configureResources
import net.bmuller.application.routing.v1.api

interface ServiceTest {
	val client: HttpClient
}

private fun Application.testModule(dependencies: Dependencies) {
	configureAuthentication(dependencies.env, dependencies.userAuthService)
	configureResources()
	configureLogging()
	configureContentNegotiation()
	routing {
		api(dependencies)
	}
}

suspend fun withService(test: suspend ServiceTest.() -> Unit) {
	val dep by KotestProject.dependencies
	withService(dep, test)
}

suspend fun createTestUserToken(): Either<DomainError, AuthTokenResponse> = either {
	val dep by KotestProject.dependencies
	val user = dep.userAuthService.signInFlow("test-auth-token").bind()
	val session = UserSession(id = user.id, version = user.authVersion, plexUsername = user.plexUsername)

	dep.userAuthService.createJwtToken(session).bind()
}

suspend fun withService(
	dependencies: Dependencies,
	test: suspend ServiceTest.() -> Unit
): Unit = testApplication {
	application { testModule(dependencies) }
	createClient {
		expectSuccess = false
		followRedirects = false
		install(Resources)
		install(ContentNegotiation) {
			json(Json {
				encodeDefaults = true
				ignoreUnknownKeys = true
				prettyPrint = true
				classDiscriminator = JsonSchemaDiscriminator
			})
		}
		install(DefaultRequest) {
			url {
				path("/api/")
			}
		}
	}.use { client ->
		test(object : ServiceTest {
			override val client: HttpClient = client
		})
	}
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
private suspend fun testApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
	val builder = ApplicationTestBuilder().apply { block() }
	val testApplication = TestApplication(builder)
	testApplication.engine.start()
	testApplication.stop()
}
