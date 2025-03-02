package com.n26.cointracker.data.network.model

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import kotlinx.serialization.Serializable

@Serializable
data class CurrentPriceResponse(val bitcoin: CurrentPriceDto)
