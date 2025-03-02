package com.n26.core.common.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class RepeatingTimerTest :
	StringSpec(
		{
			"repeatingTimer executes action repeatedly" {
				var counter = 0
				runTest {
					// Launch the repeating timer on the test dispatcher (coroutineContext)
					val job = launch {
						repeatingTimer(duration = 100.milliseconds, context = coroutineContext) {
							counter++
						}
					}

					// Initially, no action has been executed.
					counter shouldBe 0

					// Advance virtual time by 350ms. Expect 3 iterations.
					testScheduler.advanceTimeBy(350)
					counter shouldBe 3

					// Cancel the repeating timer to stop the loop.
					job.cancel()
				}
			}
		},
	)
