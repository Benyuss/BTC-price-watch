package com.n26.cointracker.core.domain.interactor

import com.n26.cointracker.core.domain.model.PriceOnDayDto

fun interface GetPriceOnDayInteractor {
	suspend operator fun invoke(
		timestamp: Long,
	): Result<PriceOnDayDto>
}
