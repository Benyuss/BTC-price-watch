package com.n26.cointracker.feature.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.n26.cointracker.core.domain.interactor.GetCurrentPriceInteractor
import com.n26.core.common.util.repeatingTimer
import com.n26.ui.common.viewmodel.BaseViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.time.Duration.Companion.minutes

class CurrentPriceViewModel(private val getCurrentPriceInteractor: GetCurrentPriceInteractor) : BaseViewModel<Double>() {
	private val autoRefreshScope = viewModelScope + SupervisorJob()

	fun refresh(
		forceRefresh: Boolean = false,
	) {
		refreshData {
			getCurrentPriceInteractor(forceRefresh).map { priceDto -> priceDto.usd }
		}
	}

	fun startAutoRefresh() {
		autoRefreshScope.launch {
			repeatingTimer(duration = 1.minutes, context = autoRefreshScope.coroutineContext) {
				refresh(forceRefresh = true)
			}
		}
	}

	fun stopAutoRefresh() {
		autoRefreshScope.coroutineContext.cancelChildren()
	}

	override fun onCleared() {
		autoRefreshScope.cancel()
		super.onCleared()
	}
}
