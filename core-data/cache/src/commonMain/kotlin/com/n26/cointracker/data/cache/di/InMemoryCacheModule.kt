package com.n26.cointracker.data.cache.di

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import com.n26.cointracker.data.cache.core.InMemoryCacheWithKeys
import com.n26.cointracker.data.cache.price.CurrentPriceCache
import com.n26.cointracker.data.cache.price.HistoricalPriceCache
import com.n26.cointracker.data.cache.price.PriceOnDayCache
import org.koin.core.qualifier.named
import org.koin.dsl.module

val inMemoryCacheModule = module {
    single<InMemoryCache<CurrentPriceDto>>(named(DiConstants.CACHE_KEY_CURRENT_PRICE)) { CurrentPriceCache() }
    single<InMemoryCache<List<HistoricalPriceDto>>>(named(DiConstants.CACHE_KEY_HISTORICAL_PRICE)) {
        HistoricalPriceCache()
    }
    single<InMemoryCacheWithKeys<PriceOnDayDto>>(named(DiConstants.CACHE_KEY_PRICE_ON_DAY)) { PriceOnDayCache() }
}
