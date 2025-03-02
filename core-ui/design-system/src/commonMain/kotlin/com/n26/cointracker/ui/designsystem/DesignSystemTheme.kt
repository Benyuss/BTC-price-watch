package com.n26.cointracker.ui.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.n26.cointracker.ui.designsystem.color.AppColors
import com.n26.cointracker.ui.designsystem.color.LightColors
import com.n26.cointracker.ui.designsystem.color.LocalColors

@Composable
fun DesignSystemTheme(
	content: @Composable () -> Unit,
) {
	CompositionLocalProvider(
		LocalColors provides LightColors,
		LocalMeasurements provides DesignSystemTheme.measurements,
	) {
		MaterialTheme(
			colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
		) {
			content()
		}
	}
}

object DesignSystemTheme {
	val colors: AppColors
		@Composable
		@ReadOnlyComposable
		get() = LocalColors.current

	val measurements: DesignSystemMeasurements
		@Composable
		@ReadOnlyComposable
		get() = LocalMeasurements.current
}
