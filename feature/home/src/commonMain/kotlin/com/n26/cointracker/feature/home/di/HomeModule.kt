package com.n26.cointracker.feature.home.di

import com.n26.cointracker.feature.home.viewmodel.CurrentPriceViewModel
import com.n26.cointracker.feature.home.viewmodel.HistoricalPricesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeModule =
	module {
		factoryOf(::CurrentPriceViewModel)
		factoryOf(::HistoricalPricesViewModel)
	}
