package com.n26.cointracker.data.network.datasource

import com.n26.cointracker.core.domain.model.CurrentPriceDto
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.data.network.BuildKonfig
import com.n26.cointracker.data.network.model.CurrentPriceResponse
import com.n26.cointracker.data.network.model.HistoricalPriceResponse
import com.n26.cointracker.data.network.model.PriceOnDayResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

interface PriceRemoteDataSource {
	suspend fun getCurrentPrice(): CurrentPriceDto

	suspend fun getPricesForDate(
		date: LocalDate,
	): Map<String, Double>

	suspend fun getPricesFromDateToDate(
		from: LocalDate,
		to: LocalDate,
	): List<HistoricalPriceDto>
}

internal class PriceRemoteDataSourceImpl(private val client: HttpClient) : PriceRemoteDataSource {
	override suspend fun getCurrentPrice(): CurrentPriceDto {
		val url =
			URLBuilder()
				.apply {
					takeFrom(BuildKonfig.coinGeckoApiUrl)
					encodedPath += "simple/price"
					parameters.append("ids", "bitcoin")
					parameters.append("vs_currencies", "usd")
					parameters.append("precision", "3")
				}.buildString()

		val response = client.get(url).body<CurrentPriceResponse>()
		return response.bitcoin
	}

	override suspend fun getPricesForDate(
		date: LocalDate,
	): Map<String, Double> {
		// Format date as dd-MM-yyyy -- API requirements
		val formattedDate = "${date.dayOfMonth}-${date.monthNumber}-${date.year}"

		val url =
			URLBuilder()
				.apply {
					takeFrom(BuildKonfig.coinGeckoApiUrl)
					encodedPath += "coins/bitcoin/history"
					parameters.append("date", formattedDate)
					parameters.append("localization", "false")
				}.buildString()

		val response = client.get(url).body<PriceOnDayResponse>()
		return response.marketData.currentPrice
	}

	override suspend fun getPricesFromDateToDate(
		from: LocalDate,
		to: LocalDate,
	): List<HistoricalPriceDto> {
		val url =
			URLBuilder()
				.apply {
					takeFrom(BuildKonfig.coinGeckoApiUrl)
					encodedPath += "coins/bitcoin/market_chart/range"
					parameters.append("vs_currency", "usd")
					parameters.append("from", from.toUnixTimestamp())
					parameters.append("to", to.toUnixTimestamp())
					parameters.append("precision", "3")
				}.buildString()

		val response = client.get(url).body<HistoricalPriceResponse>()
		return response.prices
	}

	private fun LocalDate.toUnixTimestamp(): String {
		// make sure to use UTC to align with the backend's timezone
		val epochSeconds = this.atStartOfDayIn(TimeZone.UTC).epochSeconds
		return epochSeconds.toString()
	}
}
