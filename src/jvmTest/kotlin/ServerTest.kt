import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import net.bmuller.application.mainModule
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTest {
	@Test
	fun testHome() = testApplication {
		application { mainModule() }
		val response = client.get("/")

		assertEquals(HttpStatusCode.OK, response.status)
	}
}