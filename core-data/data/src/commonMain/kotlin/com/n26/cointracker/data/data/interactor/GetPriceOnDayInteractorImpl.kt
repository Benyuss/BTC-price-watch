package com.n26.cointracker.data.data.interactor

import com.n26.cointracker.core.domain.interactor.GetPriceOnDayInteractor
import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.data.cache.core.InMemoryCacheWithKeys
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import com.n26.core.common.ktx.toLocalDate
import com.n26.core.common.ktx.toUtcDate
import com.n26.data.common.BaseInteractor

internal class GetPriceOnDayInteractorImpl(
    private val dispatchers: N26Dispatchers,
    private val priceRemoteDataSource: PriceRemoteDataSource,
    private val inMemoryCache: InMemoryCacheWithKeys<PriceOnDayDto>,
) : BaseInteractor(GetPriceOnDayInteractor::class),
    GetPriceOnDayInteractor {

    override suspend operator fun invoke(timestamp: Long): Result<PriceOnDayDto> =
        execute(
            dispatcher = dispatchers.io,
            readCache = { inMemoryCache.getState(timestamp.toString()) },
            writeCache = { inMemoryCache.setState(timestamp.toString(), it) }
        ) {
            val result = priceRemoteDataSource.getPricesForDate(timestamp.toUtcDate())
            val prices = PriceOnDayDto(
                usd = result["usd"] ?: 0.0,
                eur = result["eur"] ?: 0.0,
                gbp = result["gbp"] ?: 0.0,
                date = timestamp.toLocalDate().toString()
            )

            Result.success(prices)
        }
}