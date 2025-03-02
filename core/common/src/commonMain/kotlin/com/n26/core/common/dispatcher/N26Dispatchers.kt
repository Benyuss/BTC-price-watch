package com.n26.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

data class N26Dispatchers(
	val io: CoroutineDispatcher,
	val default: CoroutineDispatcher,
	val main: CoroutineDispatcher,
)
