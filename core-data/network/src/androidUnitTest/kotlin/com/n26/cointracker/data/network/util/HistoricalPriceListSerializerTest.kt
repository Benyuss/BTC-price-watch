package com.n26.cointracker.data.network.util

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.core.common.ktx.toLocalDate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class HistoricalPriceListSerializerTest :
	StringSpec(
		{

			"deserializes JSON array to list of HistoricalPriceDto" {
				val jsonString =
					"""
					[
					  [ 1740777600, 100.5 ],
					  [ 1740864000, 101.7 ]
					]
					""".trimIndent()

				val expected = listOf(
					HistoricalPriceDto(
						utcTimestamp = 1740777600,
						localDate = 1740777600.toLong().toLocalDate(),
						price = 100.5,
					),
					HistoricalPriceDto(
						utcTimestamp = 1740864000,
						localDate = 1740864000.toLong().toLocalDate(),
						price = 101.7,
					),
				)

				val json = Json { ignoreUnknownKeys = true }
				val result = json.decodeFromString(HistoricalPriceListSerializer, jsonString)
				result shouldBe expected
			}

			"serializes list of HistoricalPriceDto to JSON array" {
				val data = listOf(
					HistoricalPriceDto(
						utcTimestamp = 1740777600,
						localDate = 1740777600.toLong().toLocalDate(),
						price = 100.5,
					),
					HistoricalPriceDto(
						utcTimestamp = 1740864000,
						localDate = 1740864000.toLong().toLocalDate(),
						price = 101.7,
					),
				)

				val json = Json { prettyPrint = true }
				val jsonString = json.encodeToString(HistoricalPriceListSerializer, data)

				val decoded = json.decodeFromString(HistoricalPriceListSerializer, jsonString)
				decoded shouldBe data
			}
		},
	)
