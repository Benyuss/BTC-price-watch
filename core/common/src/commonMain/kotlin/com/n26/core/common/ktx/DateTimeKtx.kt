package com.n26.core.common.ktx

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toUtcDate() = Instant.fromEpochMilliseconds(this)
    .toLocalDateTime(TimeZone.UTC)
    .date

fun Long.toLocalDate() = Instant.fromEpochMilliseconds(this)
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date
