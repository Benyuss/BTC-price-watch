package com.n26.cointracker.di

import com.n26.cointracker.data.data.di.dataModule
import com.n26.cointracker.feature.details.di.detailsModule
import com.n26.cointracker.feature.home.di.homeModule
import com.n26.core.common.dispatcher.N26Dispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.dsl.module

val featureModule =
	module {
		includes(homeModule)
		includes(detailsModule)
	}

val appModule =
	module {
		single<N26Dispatchers> {
			N26Dispatchers(
				io = Dispatchers.IO,
				default = Dispatchers.Default,
				main = Dispatchers.Main,
			)
		}
	}

fun initKoin() {
	startKoin {
		modules(
			appModule,
			dataModule,
			featureModule,
		)
	}
}
