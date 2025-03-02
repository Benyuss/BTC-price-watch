package com.n26.cointracker.feature.details.viewmodel

import com.n26.cointracker.core.domain.interactor.GetPriceOnDayInteractor
import com.n26.cointracker.core.domain.model.PriceOnDayDto
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
class PriceDetailsOnDayViewModelTest :
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

			Given("a successful interactor call") {
				val expectedDto = PriceOnDayDto(
					usd = 100.0,
					eur = 90.0,
					gbp = 80.0,
					date = "2025-03-02",
				)
				val interactorMock = mockk<GetPriceOnDayInteractor>()
				coEvery { interactorMock(any()) } returns Result.success(expectedDto)

				When("refresh is called with a valid timestamp") {
					val viewModel = PriceDetailsOnDayViewModel(interactorMock)
					viewModel.refresh(dateTimeStamp = 1740777600000L)
					testDispatcher.scheduler.advanceUntilIdle()

					Then("the state should become Success with the expected DTO") {
						viewModel.state.value shouldBe Resource.Success(expectedDto)
					}
				}
			}

			Given("a failing interactor call") {
				val exception = Exception("Failure")
				val interactorMock = mockk<GetPriceOnDayInteractor>()
				coEvery { interactorMock(any()) } returns Result.failure(exception)

				When("refresh is invoked") {
					val viewModel = PriceDetailsOnDayViewModel(interactorMock)
					viewModel.refresh(dateTimeStamp = 1740777600000L)
					testDispatcher.scheduler.advanceUntilIdle()

					Then("the state should become Error with the expected message") {
						when (val state = viewModel.state.value) {
							is Resource.Error -> state.throwable.message shouldBe "Failure"
							else -> error("Expected error state")
						}
					}
				}
			}
		},
	)
