package com.n26.cointracker.data.cache.price

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow

internal class CurrentPriceCache : InMemoryCache<CurrentPriceDto> {
    private val _state = MutableStateFlow<CurrentPriceDto?>(null)

    override fun getState() = _state.value

    override fun setState(
        value: CurrentPriceDto?,
    ) {
        _state.value = value
    }
}
