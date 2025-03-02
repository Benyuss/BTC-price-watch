package com.n26.cointracker.data.cache.core

interface InMemoryCache<T> {
    fun getState(): T?

    fun setState(
        value: T?,
    )
}
