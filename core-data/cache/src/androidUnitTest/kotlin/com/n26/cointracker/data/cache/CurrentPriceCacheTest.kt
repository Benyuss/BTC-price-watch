package com.n26.cointracker.data.cache

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.data.cache.price.CurrentPriceCache
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CurrentPriceCacheTest :
	StringSpec(
		{

			"initial state should be null" {
				val cache = CurrentPriceCache()
				cache.getState() shouldBe null
			}

			"setState updates the cache" {
				val cache = CurrentPriceCache()
				val price = CurrentPriceDto(usd = 123.45)
				cache.setState(price)
				cache.getState() shouldBe price
			}

			"setting state to null resets the cache" {
				val cache = CurrentPriceCache()
				val price = CurrentPriceDto(usd = 123.45)
				cache.setState(price)
				cache.getState() shouldBe price
				cache.setState(null)
				cache.getState() shouldBe null
			}
		},
	)
