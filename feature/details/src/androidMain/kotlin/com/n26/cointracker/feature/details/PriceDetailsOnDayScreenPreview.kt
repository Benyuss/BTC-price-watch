package com.n26.cointracker.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.feature.details.ui.PriceDetailsOnDayScreenContent
import com.n26.ui.common.model.Resource

@Preview
@Composable
internal fun PriceDetailsOnDayScreenPreview() {
    PriceDetailsOnDayScreenContent(
        resource = Resource.Success(
            PriceOnDayDto(
                usd = 100.0,
                eur = 100.1,
                gbp = 100.2,
                date = "2025-03-01"
            )
        ),
        navigateBack = {}
    )
}

@Preview
@Composable
internal fun PriceDetailsOnDayScreenLoadingPreview() {
    PriceDetailsOnDayScreenContent(
        resource = Resource.Loading,
        navigateBack = {}
    )
}

@Preview
@Composable
internal fun PriceDetailsOnDayScreenErrorPreview() {
    PriceDetailsOnDayScreenContent(
        resource = Resource.Error(RuntimeException("Error")),
        navigateBack = {}
    )
}
