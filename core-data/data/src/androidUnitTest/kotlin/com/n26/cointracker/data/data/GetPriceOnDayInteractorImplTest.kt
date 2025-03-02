package com.n26.cointracker.data.data

import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.data.data.interactor.GetPriceOnDayInteractorImpl
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import com.n26.core.common.ktx.toLocalDate
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest

class GetPriceOnDayInteractorImplTest :
	BehaviorSpec(
		{
			val testScheduler = TestCoroutineScheduler()
			val testDispatcher = StandardTestDispatcher(testScheduler)
			val dispatchers = N26Dispatchers(
				io = testDispatcher,
				default = testDispatcher,
				main = testDispatcher,
			)

			val remote: PriceRemoteDataSource = mockk(relaxed = true)
			val cache: com.n26.cointracker.data.cache.core.InMemoryCacheWithKeys<PriceOnDayDto> = mockk(relaxed = true)
			val interactor = GetPriceOnDayInteractorImpl(dispatchers, remote, cache)

			afterTest {
				clearMocks(remote, cache)
			}

			Given("a price-on-day interactor") {
				When("cache is empty") {
					Then("it should fetch from remote and update the cache") {
						runTest(testDispatcher) {
							// Suppose the remote returns a map of prices:
							coEvery { remote.getPricesForDate(any()) } returns mapOf(
								"usd" to 100.0,
								"eur" to 90.0,
								"gbp" to 80.0,
							)
							// Cache returns null.
							coEvery { cache.getState(any()) } returns null

							val timestamp = 1740777600000L // For example
							val result = interactor(timestamp)
							// Build expected PriceOnDayDto using conversion functions from your code.
							val expected = PriceOnDayDto(
								usd = 100.0,
								eur = 90.0,
								gbp = 80.0,
								date = timestamp.toLocalDate().toString(),
							)
							result shouldBe Result.success(expected)
						}
					}
				}
			}
		},
	)
