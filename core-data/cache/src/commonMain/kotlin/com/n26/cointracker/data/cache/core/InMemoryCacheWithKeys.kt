package com.n26.cointracker.data.cache.core

interface InMemoryCacheWithKeys<T> {
	fun getState(
		cacheId: String,
	): T?

	fun setState(
		cacheId: String,
		value: T?,
	)
}
