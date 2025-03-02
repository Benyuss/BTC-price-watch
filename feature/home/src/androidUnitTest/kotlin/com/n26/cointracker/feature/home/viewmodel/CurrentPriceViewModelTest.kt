package com.n26.cointracker.feature.home.viewmodel

import com.n26.cointracker.core.domain.interactor.GetCurrentPriceInteractor
import com.n26.cointracker.core.domain.model.CurrentPriceDto
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
class CurrentPriceViewModelTest :
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

			Given("a successful current price interactor") {
				val expectedPrice = 100.0
				val interactorMock = mockk<GetCurrentPriceInteractor>()
				coEvery { interactorMock(any()) } returns Result.success(CurrentPriceDto(expectedPrice))

				When("refresh is invoked with forceRefresh false") {
					val viewModel = CurrentPriceViewModel(interactorMock)
					viewModel.refresh(forceRefresh = false)
					testDispatcher.scheduler.advanceUntilIdle()

					Then("the state should become Success with the expected price") {
						viewModel.state.value shouldBe Resource.Success(expectedPrice)
					}
				}
			}

			Given("a failing current price interactor") {
				val exception = Exception("Failure")
				val interactorMock = mockk<GetCurrentPriceInteractor>()
				coEvery { interactorMock(any()) } returns Result.failure(exception)

				When("refresh is invoked with forceRefresh false") {
					val viewModel = CurrentPriceViewModel(interactorMock)
					viewModel.refresh(forceRefresh = false)
					testDispatcher.scheduler.advanceUntilIdle()

					Then("the state should become Error with the expected exception message") {
						when (val state = viewModel.state.value) {
							is Resource.Error -> state.throwable.message shouldBe "Failure"
							else -> error("Expected an error state")
						}
					}
				}
			}
		},
	)
