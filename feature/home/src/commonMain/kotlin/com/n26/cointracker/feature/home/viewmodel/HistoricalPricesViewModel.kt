package com.n26.cointracker.feature.home.viewmodel

import com.n26.cointracker.core.domain.interactor.GetHistoricalPriceInteractor
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.ui.common.viewmodel.BaseViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

class HistoricalPricesViewModel(
    private val getHistoricalPriceInteractor: GetHistoricalPriceInteractor
) : BaseViewModel<List<HistoricalPriceDto>>() {

    fun refresh() {
        refreshData {
            val today = Clock.System.todayIn(TimeZone.UTC)
            val rangeStart = today.minus(DatePeriod(days = PRICE_PERIOD_DAYS))
            getHistoricalPriceInteractor(rangeStart, today)
        }
    }

    companion object {
        const val PRICE_PERIOD_DAYS = 14
    }
}
