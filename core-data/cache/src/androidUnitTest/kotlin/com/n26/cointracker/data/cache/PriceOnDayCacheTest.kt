package com.n26.cointracker.data.cache

import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.data.cache.price.PriceOnDayCache
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PriceOnDayCacheTest :
	StringSpec(
		{

			"initial state for any key should be null" {
				val cache = PriceOnDayCache()
				cache.getState("someKey") shouldBe null
			}

			"setState updates the cache for a given key" {
				val cache = PriceOnDayCache()
				val priceDto = PriceOnDayDto(usd = 100.0, eur = 90.0, gbp = 80.0, date = "2025-03-02")
				cache.setState("someKey", priceDto)
				cache.getState("someKey") shouldBe priceDto
			}

			"setting a key to null resets its value" {
				val cache = PriceOnDayCache()
				val priceDto = PriceOnDayDto(usd = 100.0, eur = 90.0, gbp = 80.0, date = "2025-03-02")
				cache.setState("someKey", priceDto)
				cache.getState("someKey") shouldBe priceDto
				cache.setState("someKey", null)
				cache.getState("someKey") shouldBe null
			}
		},
	)
