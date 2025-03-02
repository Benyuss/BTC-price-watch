package com.n26.cointracker.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DesignSystemMeasurements(
	val spacing0: Dp,
	val spacing1: Dp,
	val spacing2: Dp,
	val spacing3: Dp,
	val spacing4: Dp,
	val spacing5: Dp,
	val spacing6: Dp,
	val spacing8: Dp,
	val spacing10: Dp,
	val spacing12: Dp,
	val spacing14: Dp,
	val spacing16: Dp,
	val spacing20: Dp,
	val spacing30: Dp,
)

internal val LocalMeasurements: ProvidableCompositionLocal<DesignSystemMeasurements> =
	staticCompositionLocalOf {
		DesignSystemMeasurements(
			spacing0 = 0.dp,
			spacing1 = 4.dp,
			spacing2 = 8.dp,
			spacing3 = 12.dp,
			spacing4 = 16.dp,
			spacing5 = 20.dp,
			spacing6 = 24.dp,
			spacing8 = 32.dp,
			spacing10 = 40.dp,
			spacing12 = 48.dp,
			spacing14 = 56.dp,
			spacing16 = 64.dp,
			spacing20 = 80.dp,
			spacing30 = 120.dp,
		)
	}
