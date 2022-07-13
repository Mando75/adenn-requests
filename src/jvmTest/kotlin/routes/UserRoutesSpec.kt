package routes

import KotestProject
import http.UserResource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import withService

class UserRoutesSpec : StringSpec({
	val dependencies by KotestProject.dependencies

	"Returns unauthenticated if no token or session" {
		withService(dependencies) {
			val response = client.get(UserResource.Me())

			response.status shouldBe HttpStatusCode.Unauthorized
		}
	}
})