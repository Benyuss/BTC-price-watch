package com.n26.cointracker.data.data.interactor

import com.n26.cointracker.core.domain.interactor.GetCurrentPriceInteractor
import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import com.n26.data.common.BaseInteractor

internal class GetCurrentPriceInteractorImpl(
    private val dispatchers: N26Dispatchers,
    private val priceRemoteDataSource: PriceRemoteDataSource,
    private val inMemoryCache: InMemoryCache<CurrentPriceDto>,
) : BaseInteractor(GetCurrentPriceInteractor::class),
    GetCurrentPriceInteractor {

    override suspend operator fun invoke(forceRefresh: Boolean): Result<CurrentPriceDto> = execute(
        dispatcher = dispatchers.io,
        readCache = { inMemoryCache.getState() },
        writeCache = { inMemoryCache.setState(it) },
        forceRefresh = forceRefresh
    ) {
        val value = priceRemoteDataSource.getCurrentPrice()
        Result.success(value)
    }
}