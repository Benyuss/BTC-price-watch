package com.n26.cointracker.data.network.model

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.network.util.HistoricalPriceListSerializer
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalPriceResponse(
	@Serializable(with = HistoricalPriceListSerializer::class)
	val prices: List<HistoricalPriceDto>,
)
