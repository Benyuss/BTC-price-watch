package com.n26.cointracker.core.domain.interactor

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import kotlinx.datetime.LocalDate

fun interface GetHistoricalPriceInteractor {
    suspend operator fun invoke(
        fromDate: LocalDate,
        toDate: LocalDate
    ): Result<List<HistoricalPriceDto>>
}
