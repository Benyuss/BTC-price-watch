package com.n26.cointracker.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentPriceDto(val usd: Double)
