package com.n26.ui.common.viewmodel

import com.n26.ui.common.model.Resource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

// A simple TestViewModel for testing purposes
class TestViewModel : BaseViewModel<String>() {
	fun testRefresh(
		success: Boolean,
	) {
		refreshData {
			if (success) {
				Result.success("Success!")
			} else {
				Result.failure(Exception("Failure"))
			}
		}
	}
}

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest :
	BehaviorSpec(
		{
			val testDispatcher = StandardTestDispatcher()

			beforeTest {
				Dispatchers.setMain(testDispatcher)
			}
			afterTest {
				Dispatchers.resetMain()
			}

			Given("a TestViewModel") {
				When("the action returns failure") {
					Then("the state should become Resource.Error") {
						runTest {
							val viewModel = TestViewModel()
							viewModel.testRefresh(success = false)
							// Advance the test dispatcher until idle so that all coroutines finish.
							testDispatcher.scheduler.advanceUntilIdle()

							viewModel.state.value.shouldBeInstanceOf<Resource.Error>()
						}
					}
				}
				When("the action returns success") {
					Then("the state should become Resource.Success with the proper value") {
						runTest {
							val viewModel = TestViewModel()
							viewModel.testRefresh(success = true)
							testDispatcher.scheduler.advanceUntilIdle()

							viewModel.state.value shouldBe Resource.Success("Success!")
						}
					}
				}
			}
		},
	)
