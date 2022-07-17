package routes

import KotestProject
import arrow.core.continuations.either
import createTestUserToken
import entities.LoginUrlResponse
import http.AuthResource
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import withService
import java.net.URLEncoder

class AuthRoutesSpec : DescribeSpec({
	val dependencies by KotestProject.dependencies
	describe("Plex Login Url") {
		it("should return a login url response") {
			withService {
				val forwardHost = "http://localhost:8080"
				val response = client.get(AuthResource.Plex.LoginUrl(forwardHost = forwardHost))


				response.status shouldBe HttpStatusCode.OK

				assertSoftly {
					val productParam = URLEncoder.encode(
						"context[device][product]",
						"utf-8"
					)
					val productValue = URLEncoder.encode(
						dependencies.env.plex.product,
						"utf-8"
					)
					val deviceParam = URLEncoder.encode("context[device][device]", "utf-8")
					val deviceValue = URLEncoder.encode(
						dependencies.env.plex.device,
						"utf-8"
					)
					val forwardHostValue = URLEncoder.encode(
						"$forwardHost/api/v1/auth/plex/callback",
						"utf-8"
					)

					val pin: LoginUrlResponse = response.body()

					pin.pinId shouldBe 1
					pin.loginUrl shouldContain "https://${dependencies.env.plex.authHost}/${dependencies.env.plex.authPath}#!?"
					pin.loginUrl shouldContain "code=test-code"
					pin.loginUrl shouldContain "$productParam=$productValue"
					pin.loginUrl shouldContain "$deviceParam=$deviceValue"
					pin.loginUrl shouldContain "clientID=test-id"
					pin.loginUrl shouldContain "forwardUrl=$forwardHostValue"
				}
			}
		}
	}

	describe("Plex auth callback") {
		it("Should redirect to login on success") {
			withService {
				val response = client.get(AuthResource.Plex.Callback(pinId = "1"))

				response.status shouldBe HttpStatusCode.Found
			}
		}
	}

	describe("Logout") {
		it("Should clear the session cookie and redirect") {
			withService {
				either {
					val (token) = createTestUserToken().bind()
					val response = client.get(AuthResource.Logout()) {
						header("Authorization", "Bearer $token")
					}

					response.status shouldBe HttpStatusCode.Found
				}
			}
		}
	}
})