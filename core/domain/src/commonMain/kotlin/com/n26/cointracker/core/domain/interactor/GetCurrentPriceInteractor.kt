package com.n26.cointracker.core.domain.interactor

import com.n26.cointracker.core.domain.model.CurrentPriceDto

fun interface GetCurrentPriceInteractor {
    suspend operator fun invoke(forceRefresh: Boolean): Result<CurrentPriceDto>
}
