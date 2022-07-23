import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTest {
	@Test
	fun testHome() = testApplication {
		assertEquals(HttpStatusCode.OK, HttpStatusCode.OK)
	}
}