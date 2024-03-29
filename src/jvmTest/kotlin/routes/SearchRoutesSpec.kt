package routes

import arrow.core.continuations.either
import createTestUserToken
import entities.SearchResultEntity
import http.SearchResource
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import matchers.shouldHaveHttpStatus
import withService

class SearchRoutesSpec : DescribeSpec({
	describe("Multi Entity Search") {
		it("return a transformed list of search results") {
			withService {
				either {
					val (token) = createTestUserToken().bind()

					val response = client.get(SearchResource.Multi(searchTerm = "Attack on Titan")) {
						header("Authorization", "Bearer $token")
					}

					response.shouldHaveHttpStatus(HttpStatusCode.OK)

					assertSoftly {
						val result: List<SearchResultEntity> = response.body()

						result.shouldHaveAtLeastSize(10)
						result.filterIsInstance<SearchResultEntity.MovieResult>().shouldHaveAtLeastSize(7)
						result.filterIsInstance<SearchResultEntity.TVResult>().shouldHaveAtLeastSize(3)
					}
				}
			}
		}
	}

	describe("Movie Search") {
		it("return a transformed list of search result") {
			withService {
				either {
					val (token) = createTestUserToken().bind()

					val response = client.get(SearchResource.Movie(searchTerm = "Attack on Titan")) {
						header("Authorization", "Bearer $token")
					}

					response.shouldHaveHttpStatus(HttpStatusCode.OK)

					assertSoftly {
						val result: List<SearchResultEntity.MovieResult> = response.body()

						result.shouldHaveAtLeastSize(7)
					}
				}
			}
		}
	}

	describe("TV Search") {
		it("return a transformed list of search result") {
			withService {
				either {
					val (token) = createTestUserToken().bind()

					val response = client.get(SearchResource.TV(searchTerm = "Attack on Titan")) {
						header("Authorization", "Bearer $token")
					}

					response.shouldHaveHttpStatus(HttpStatusCode.OK)

					assertSoftly {
						val result: List<SearchResultEntity.TVResult> = response.body()

						result.shouldHaveAtLeastSize(3)
					}
				}
			}
		}
	}
})