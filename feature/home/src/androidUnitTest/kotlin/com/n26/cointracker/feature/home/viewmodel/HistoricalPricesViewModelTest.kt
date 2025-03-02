package com.n26.cointracker.feature.home.viewmodel

import com.n26.cointracker.core.domain.interactor.GetHistoricalPriceInteractor
import com.n26.ui.common.model.Resource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HistoricalPricesViewModelTest :
	BehaviorSpec(
		{

			val testDispatcher = StandardTestDispatcher()

			beforeTest {
				Dispatchers.setMain(testDispatcher)
			}
			afterTest {
				Dispatchers.resetMain()
				clearAllMocks()
			}

			given("a failing historical prices interactor") {
				val exception = Exception("Failure")
				val interactorMock = mockk<GetHistoricalPriceInteractor>()
				// Respond with failure for any input dates:
				coEvery { interactorMock(any(), any()) } returns Result.failure(exception)

				`when`("refresh is invoked") {
					val viewModel = HistoricalPricesViewModel(interactorMock)
					viewModel.refresh()
					testDispatcher.scheduler.advanceUntilIdle()

					then("the state should become Error with the expected exception message") {
						when (val state = viewModel.state.value) {
							is Resource.Error -> state.throwable.message shouldBe "Failure"
							else -> error("Expected an error state")
						}
					}
				}
			}
		},
	)
