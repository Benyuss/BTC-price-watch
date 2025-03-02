package com.n26.data.common

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest

class TestInteractor(private val dispatcher: TestDispatcher) : BaseInteractor(TestInteractor::class) {
	suspend fun <T> runAction(
		forceRefresh: Boolean = false,
		cacheValue: T? = null,
		actionResult: Result<T>,
	): Result<T> = execute(
		dispatcher = dispatcher,
		readCache = { cacheValue },
		writeCache = { /* optionally capture write event */ },
		forceRefresh = forceRefresh,
	) { actionResult }
}

class BaseInteractorTest :
	BehaviorSpec(
		{

			Given("a BaseInteractor") {
				// Create a single TestCoroutineScheduler and a corresponding dispatcher
				val testScheduler = TestCoroutineScheduler()
				val testDispatcher = StandardTestDispatcher(testScheduler)
				val interactor = TestInteractor(testDispatcher) // TestInteractor extends BaseInteractor

				When("the cache is empty") {
					Then("it should execute the action and return the new value") {
						runTest(testDispatcher) {
							val result = interactor.runAction(
								forceRefresh = false,
								cacheValue = null,
								actionResult = Result.success("new value"),
							)
							result shouldBe Result.success("new value")
						}
					}
				}

				When("the cache is non-empty but refresh is forced") {
					Then("it should execute the action and return the new value") {
						runTest(testDispatcher) {
							val result = interactor.runAction(
								forceRefresh = true,
								cacheValue = "old value",
								actionResult = Result.success("new value"),
							)
							result shouldBe Result.success("new value")
						}
					}
				}

				When("the cache has a value and refresh is not forced") {
					Then("it should return the cached value without executing the action") {
						runTest(testDispatcher) {
							val cachedValue = "cached value"
							val result = interactor.runAction(
								forceRefresh = false,
								cacheValue = cachedValue,
								actionResult = Result.success("new value"),
							)
							result shouldBe Result.success(cachedValue)
						}
					}
				}

				When("the action fails") {
					Then("it should propagate the failure") {
						runTest(testDispatcher) {
							val error = Exception("Test error")
							val result = interactor.runAction(
								forceRefresh = false,
								cacheValue = null,
								actionResult = Result.failure(error),
							)
							result.exceptionOrNull() shouldBe error
						}
					}
				}
			}
		},
	)
