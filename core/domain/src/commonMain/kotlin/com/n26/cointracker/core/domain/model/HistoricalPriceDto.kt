package com.n26.cointracker.core.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalPriceDto(
	val utcTimestamp: Long,
	val localDate: LocalDate,
	val price: Double,
)
