package com.n26.core.common.ktx

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateConversionTest :
	StringSpec(
		{

			"toUtcDate returns correct date for known epoch millis" {
				val epochMillis = 1740777600000L
				val expectedDate = Instant
					.fromEpochMilliseconds(epochMillis)
					.toLocalDateTime(TimeZone.UTC)
					.date
				val result = epochMillis.toUtcDate()
				result shouldBe expectedDate
			}

			"toLocalDate returns correct date for known epoch millis" {
				val epochMillis = 1740777600000L
				// Compute expected date using the same conversion logic.
				val expectedDate = Instant
					.fromEpochMilliseconds(epochMillis)
					.toLocalDateTime(TimeZone.currentSystemDefault())
					.date
				val result = epochMillis.toLocalDate()
				result shouldBe expectedDate
			}
		},
	)
