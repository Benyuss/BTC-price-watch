package com.n26.cointracker.data.data

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import com.n26.cointracker.data.data.interactor.GetCurrentPriceInteractorImpl
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest

class GetCurrentPriceInteractorImplTest :
	BehaviorSpec(
		{
			// Create a single scheduler and dispatcher
			val testScheduler = TestCoroutineScheduler()
			val testDispatcher = StandardTestDispatcher(testScheduler)
			val dispatchers = N26Dispatchers(
				io = testDispatcher,
				default = testDispatcher,
				main = testDispatcher,
			)

			// Create mocks for the remote data source and cache.
			val remote: PriceRemoteDataSource = mockk(relaxed = true)
			val cache: InMemoryCache<CurrentPriceDto> = mockk(relaxed = true)

			// Instantiate the interactor.
			val interactor = GetCurrentPriceInteractorImpl(dispatchers, remote, cache)

			afterTest {
				clearMocks(remote, cache)
			}

			Given("a current price interactor") {
				When("the cache is not forced and returns a value") {
					Then("it should return the cached value and not call remote") {
						runTest(testDispatcher) {
							val cachedValue = CurrentPriceDto(usd = 50.0)
							// Set cache mock to return cached value.
							coEvery { cache.getState() } returns cachedValue

							val result = interactor(forceRefresh = false)
							result shouldBe Result.success(cachedValue)
							// Verify that the remote wasn't called.
							coVerify(exactly = 0) { remote.getCurrentPrice() }
						}
					}
				}

				When("the cache is empty or force refresh is true") {
					Then("it should call the remote, update the cache, and return the remote value") {
						runTest(testDispatcher) {
							// Cache returns null.
							coEvery { cache.getState() } returns null
							val remoteValue = CurrentPriceDto(usd = 100.0)
							coEvery { remote.getCurrentPrice() } returns remoteValue

							val result = interactor(forceRefresh = false)
							result shouldBe Result.success(remoteValue)
							// Verify that the remote was called once.
							coVerify(exactly = 1) { remote.getCurrentPrice() }
						}
					}
				}

				When("forceRefresh is true even if cache has a value") {
					Then("it should call the remote and update the cache") {
						runTest(testDispatcher) {
							val cachedValue = CurrentPriceDto(usd = 50.0)
							coEvery { cache.getState() } returns cachedValue
							val remoteValue = CurrentPriceDto(usd = 100.0)
							coEvery { remote.getCurrentPrice() } returns remoteValue

							val result = interactor(forceRefresh = true)
							result shouldBe Result.success(remoteValue)
							coVerify(exactly = 1) { remote.getCurrentPrice() }
						}
					}
				}
			}
		},
	)
