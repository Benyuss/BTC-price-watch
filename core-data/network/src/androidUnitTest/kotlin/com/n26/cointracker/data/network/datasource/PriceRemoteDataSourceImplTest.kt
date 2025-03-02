package com.n26.cointracker.data.network.datasource

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class PriceRemoteDataSourceImplTest :
	BehaviorSpec(
		{
			Given("a valid current price response from the remote service") {
				// Prepare a fake JSON response (matching the CurrentPriceResponse structure)
				val jsonResponse =
					"""
					{
					  "bitcoin": {
					    "usd": 123.456
					  }
					}
					""".trimIndent()

				// Create a Ktor MockEngine that returns the above JSON when the URL contains "simple/price"
				val mockEngine = MockEngine { request ->
					if (request.url.fullPath.contains("simple/price")) {
						respond(
							content = jsonResponse,
							status = HttpStatusCode.OK,
							headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
						)
					} else {
						error("Unhandled URL: ${request.url}")
					}
				}

				// Build an HttpClient with the MockEngine and ContentNegotiation installed.
				val client = HttpClient(mockEngine) {
					install(ContentNegotiation) {
						json(Json { ignoreUnknownKeys = true })
					}
				}

				// Instantiate the remote data source with the fake client.
				val dataSource = PriceRemoteDataSourceImpl(client)

				When("getCurrentPrice is invoked") {
					Then("it should return the expected CurrentPriceDto value") {
						runTest {
							val result = dataSource.getCurrentPrice()
							result shouldBe CurrentPriceDto(usd = 123.456)
						}
					}
				}
			}
		},
	)
