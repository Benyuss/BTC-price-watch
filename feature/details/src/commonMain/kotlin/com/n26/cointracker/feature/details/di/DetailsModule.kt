package com.n26.cointracker.feature.details.di

import com.n26.cointracker.feature.details.viewmodel.PriceDetailsOnDayViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val detailsModule =
	module {
		factoryOf(::PriceDetailsOnDayViewModel)
	}
