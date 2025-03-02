package com.n26.cointracker.core.domain.model

data class PriceOnDayDto(
	val usd: Double,
	val eur: Double,
	val gbp: Double,
	val date: String,
)
