package com.n26.cointracker.feature.details.viewmodel

import com.n26.cointracker.core.domain.interactor.GetPriceOnDayInteractor
import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.ui.common.viewmodel.BaseViewModel

class PriceDetailsOnDayViewModel(
    private val getPriceOnDayInteractor: GetPriceOnDayInteractor,
) : BaseViewModel<PriceOnDayDto>() {

    fun refresh(dateTimeStamp: Long) {
        refreshData {
            getPriceOnDayInteractor(dateTimeStamp)
        }
    }
}
