package com.n26.cointracker.data.data.di

import com.n26.cointracker.core.domain.interactor.GetCurrentPriceInteractor
import com.n26.cointracker.core.domain.interactor.GetHistoricalPriceInteractor
import com.n26.cointracker.core.domain.interactor.GetPriceOnDayInteractor
import com.n26.cointracker.data.cache.di.DiConstants
import com.n26.cointracker.data.cache.di.inMemoryCacheModule
import com.n26.cointracker.data.data.interactor.GetCurrentPriceInteractorImpl
import com.n26.cointracker.data.data.interactor.GetHistoricalPriceInteractorImpl
import com.n26.cointracker.data.data.interactor.GetPriceOnDayInteractorImpl
import com.n26.cointracker.data.network.di.networkModule
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val dataModule =
	module {
		includes(inMemoryCacheModule)
		includes(networkModule)

		single<GetHistoricalPriceInteractor> {
			GetHistoricalPriceInteractorImpl(
				dispatchers = get(),
				priceRemoteDataSource = get(),
				inMemoryCache = get(qualifier(DiConstants.CACHE_KEY_HISTORICAL_PRICE)),
			)
		}
		single<GetCurrentPriceInteractor> {
			GetCurrentPriceInteractorImpl(
				dispatchers = get(),
				priceRemoteDataSource = get(),
				inMemoryCache = get(qualifier(DiConstants.CACHE_KEY_CURRENT_PRICE)),
			)
		}
		single<GetPriceOnDayInteractor> {
			GetPriceOnDayInteractorImpl(
				dispatchers = get(),
				priceRemoteDataSource = get(),
				inMemoryCache = get(qualifier(DiConstants.CACHE_KEY_PRICE_ON_DAY)),
			)
		}
	}
