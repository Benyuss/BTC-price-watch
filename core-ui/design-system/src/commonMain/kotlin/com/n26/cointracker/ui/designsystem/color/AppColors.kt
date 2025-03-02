package com.n26.cointracker.ui.designsystem.color

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalColors: ProvidableCompositionLocal<AppColors> = staticCompositionLocalOf { LightColors }

internal val LightColors: AppColors = AppColors(
    brandColor = Color(0xFF009688),
    secondaryColor = Color(0xFFE6E6E6)
)

data class AppColors(val brandColor: Color, val secondaryColor: Color)
