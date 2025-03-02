package com.n26.cointracker.data.cache.price

import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.data.cache.core.InMemoryCacheWithKeys
import kotlinx.coroutines.flow.MutableStateFlow

internal class PriceOnDayCache : InMemoryCacheWithKeys<PriceOnDayDto> {
    private val _state = MutableStateFlow<Map<String, PriceOnDayDto?>>(emptyMap())

    override fun getState(
        cacheId: String,
    ) = _state.value[cacheId]

    override fun setState(
        cacheId: String,
        value: PriceOnDayDto?,
    ) {
        _state.value = _state.value.plus(cacheId to value)
    }
}