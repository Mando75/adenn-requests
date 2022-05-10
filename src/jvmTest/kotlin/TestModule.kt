import io.ktor.server.application.*
import net.bmuller.application.plugins.configureContentNegotiation
import net.bmuller.application.plugins.configureDI
import net.bmuller.application.plugins.configureResources

fun Application.testModule() {
	configureDI()
	configureContentNegotiation()
	configureResources()
}