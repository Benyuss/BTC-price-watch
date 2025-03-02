package com.n26.cointracker.data.cache

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.cache.price.HistoricalPriceCache
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class HistoricalPriceCacheTest :
	StringSpec(
		{

			"initial state should be null" {
				val cache = HistoricalPriceCache()
				cache.getState() shouldBe null
			}

			"setState updates the cache" {
				val cache = HistoricalPriceCache()
				val list = listOf(
					HistoricalPriceDto(utcTimestamp = 12345, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 2), price = 100.0),
					HistoricalPriceDto(utcTimestamp = 12346, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 3), price = 101.0),
				)
				cache.setState(list)
				cache.getState() shouldBe list
			}

			"setting state to null resets the cache" {
				val cache = HistoricalPriceCache()
				val list = listOf(
					HistoricalPriceDto(utcTimestamp = 12345, localDate = LocalDate(year = 2025, monthNumber = 3, dayOfMonth = 2), price = 100.0),
				)
				cache.setState(list)
				cache.getState() shouldBe list
				cache.setState(null)
				cache.getState() shouldBe null
			}
		},
	)
