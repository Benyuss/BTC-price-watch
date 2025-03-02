package com.n26.cointracker.data.data.interactor

import com.n26.cointracker.core.domain.interactor.GetHistoricalPriceInteractor
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.cache.core.InMemoryCache
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.core.common.dispatcher.N26Dispatchers
import com.n26.core.common.ktx.toUtcDate
import com.n26.data.common.BaseInteractor
import kotlinx.datetime.LocalDate

internal class GetHistoricalPriceInteractorImpl(
	private val dispatchers: N26Dispatchers,
	private val priceRemoteDataSource: PriceRemoteDataSource,
	private val inMemoryCache: InMemoryCache<List<HistoricalPriceDto>>,
) : BaseInteractor(GetHistoricalPriceInteractor::class),
	GetHistoricalPriceInteractor {
	override suspend operator fun invoke(
		fromDate: LocalDate,
		toDate: LocalDate,
	): Result<List<HistoricalPriceDto>> = execute(
		dispatcher = dispatchers.io,
		readCache = { inMemoryCache.getState() },
		writeCache = { inMemoryCache.setState(it) },
	) {
		val allDataPoints = priceRemoteDataSource.getPricesFromDateToDate(fromDate, toDate)

		val groupedByDay: Map<LocalDate, List<HistoricalPriceDto>> =
			allDataPoints.groupBy {
				it.utcTimestamp.toUtcDate()
			}

		val dailyData =
			groupedByDay.mapNotNull { (_, dayList) ->
				dayList.maxByOrNull { dataPoint -> dataPoint.utcTimestamp }
			}

		val sortedDaily = dailyData.sortedByDescending { it.utcTimestamp }

		Result.success(sortedDaily)
	}
}
