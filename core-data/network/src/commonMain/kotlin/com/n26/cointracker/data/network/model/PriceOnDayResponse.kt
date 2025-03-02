package com.n26.cointracker.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceOnDayResponse(
    @SerialName("market_data")
    val marketData: MarketDataResponse,
)

@Serializable
data class MarketDataResponse(
    @SerialName("current_price")
    val currentPrice: Map<String, Double>,
)
