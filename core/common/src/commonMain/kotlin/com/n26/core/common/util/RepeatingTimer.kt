package com.n26.core.common.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

fun CoroutineScope.repeatingTimer(
	duration: Duration,
	context: CoroutineContext,
	action: suspend () -> Unit,
) {
	launch(context) {
		while (true) {
			delay(duration)
			action()
		}
	}
}
