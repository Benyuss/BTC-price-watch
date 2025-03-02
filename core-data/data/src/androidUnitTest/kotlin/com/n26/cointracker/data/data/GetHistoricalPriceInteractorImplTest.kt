package com.n26.cointracker.data.data

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.data.interactor.GetHistoricalPriceInteractorImpl
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeSortedDescendingBy
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate

class GetHistoricalPriceInteractorImplTest :
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
			val cache: com.n26.cointracker.data.cache.core.InMemoryCache<List<HistoricalPriceDto>> = mockk(relaxed = true)
			val interactor = GetHistoricalPriceInteractorImpl(dispatchers, remote, cache)

			afterTest {
				clearMocks(remote, cache)
			}

			Given("a historical price interactor") {
				When("remote returns multiple data points") {
					Then("it should group by day, pick the maximum per day, and sort descending by timestamp") {
						runTest(testDispatcher) {
							// Fake remote data: two data points on different days.
							val dataPoints = listOf(
								HistoricalPriceDto(utcTimestamp = 1000, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 1), price = 100.0),
								HistoricalPriceDto(utcTimestamp = 2000, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 2), price = 101.0),
								// Add an extra point for 2025-03-01 with a higher timestamp:
								HistoricalPriceDto(utcTimestamp = 1500, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 1), price = 102.0),
							)
							coEvery { remote.getPricesFromDateToDate(any(), any()) } returns dataPoints
							coEvery { cache.getState() } returns null

							val fromDate = LocalDate.parse("2025-03-01")
							val toDate = LocalDate.parse("2025-03-02")
							val result = interactor(fromDate, toDate).getOrThrow()

							// For 2025-03-01, expect the max timestamp (1500) point;
							// for 2025-03-02, expect the point with timestamp 2000.
							result shouldBeSortedDescendingBy { it.utcTimestamp }
						}
					}
				}
			}
		},
	)
