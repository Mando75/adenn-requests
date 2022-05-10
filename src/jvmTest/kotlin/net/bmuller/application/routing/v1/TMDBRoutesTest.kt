package net.bmuller.application.routing.v1;

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import testModule
import kotlin.test.Test
import kotlin.test.assertEquals

class TMDBRoutesTest {

	@Test
	fun testGet() = testApplication {
		val client = createClient {
			install(Resources)
			install(ContentNegotiation) { json() }
		}
		application {
			testModule()
			routing {
				tmdb()
			}
		}
		client.get(TMDBResource.Search.Movies(searchTerm = "Star Wars")).apply {
			assertEquals(this.status, HttpStatusCode.OK)
		}
	}
}