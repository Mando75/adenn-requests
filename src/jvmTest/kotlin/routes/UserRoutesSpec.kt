package routes

import arrow.core.continuations.either
import createTestUserToken
import entities.UserEntity
import http.UserResource
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import withService

class UserRoutesSpec : StringSpec({

	"Returns unauthenticated if no token or session" {
		withService {
			val response = client.get(UserResource.Me())

			response.status shouldBe HttpStatusCode.Unauthorized
		}
	}

	"Returns an error if invalid token is provided" {
		withService {
			val response = client.get(UserResource.Me()) {
				header("Authorization", "Bearer bad-token")
			}

			response.status shouldBe HttpStatusCode.Unauthorized
		}
	}

	"Returns the user object if authenticated" {
		withService {
			either {
				val (token) = createTestUserToken().bind()

				val response = client.get(UserResource.Me()) {
					header("Authorization", "Bearer $token")
				}

				response.status shouldBe HttpStatusCode.OK

				assertSoftly {
					val user: UserEntity = response.body()

					user.plexUsername shouldBe "testuser"
					user.email shouldBe "test@bmuller.net"
					user.authVersion shouldBe 0
					user.plexId shouldBe 1
					user.requestCount shouldBe 0
					user.movieQuotaDays shouldBe 1
					user.movieQuotaLimit shouldBe 5
					user.tvQuotaDays shouldBe 1
					user.tvQuotaLimit shouldBe 5
				}
			}
		}
	}
})