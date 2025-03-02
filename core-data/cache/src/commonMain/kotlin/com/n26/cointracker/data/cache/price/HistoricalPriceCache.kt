package com.n26.cointracker.data.cache.price

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import kotlinx.coroutines.flow.MutableStateFlow

internal class HistoricalPriceCache : InMemoryCache<List<HistoricalPriceDto>> {
	private val _state = MutableStateFlow<List<HistoricalPriceDto>?>(null)

	override fun getState() = _state.value

	override fun setState(
		value: List<HistoricalPriceDto>?,
	) {
		_state.value = value
	}
}
