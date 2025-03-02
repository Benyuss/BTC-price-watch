package com.n26.cointracker.feature.home.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.feature.home.ui.HomeScreenContent
import com.n26.ui.common.model.Resource
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import kotlin.time.Duration.Companion.days

@Preview
@Composable
internal fun HomeScreenContentPreview() {
    val date = Clock.System.todayIn(TimeZone.UTC)

    Surface {
        HomeScreenContent(
            currentPriceUiState = Resource.Success(1.0),
            historicalPricesUiState = Resource.Success(
                listOf(
                    HistoricalPriceDto(
                        utcTimestamp = date.atStartOfDayIn(TimeZone.UTC).epochSeconds,
                        localDate = date,
                        price = 100.5,
                    ),
                    HistoricalPriceDto(
                        utcTimestamp = date.atStartOfDayIn(TimeZone.UTC).minus(1.days).epochSeconds,
                        localDate = date,
                        price = 100.7,
                    ),
                    HistoricalPriceDto(
                        utcTimestamp = date.atStartOfDayIn(TimeZone.UTC).minus(2.days).epochSeconds,
                        localDate = date,
                        price = 100.9,
                    )
                )
            ),
            navigateToDetails = {}
        )
    }
}

@Preview
@Composable
internal fun HomeScreenContentLoadingAllPreview() {
    Surface {
        HomeScreenContent(
            currentPriceUiState = Resource.Loading,
            historicalPricesUiState = Resource.Loading,
            navigateToDetails = {}
        )
    }
}

@Preview
@Composable
internal fun HomeScreenContentLoadingHistoricalPricesPreview() {
    Surface {
        HomeScreenContent(
            currentPriceUiState = Resource.Success(1.0),
            historicalPricesUiState = Resource.Loading,
            navigateToDetails = {}
        )
    }
}

@Preview
@Composable
internal fun HomeScreenContentEmptyHistoricalPricesPreview() {
    Surface {
        HomeScreenContent(
            currentPriceUiState = Resource.Success(1.0),
            historicalPricesUiState = Resource.Success(
                emptyList()
            ),
            navigateToDetails = {}
        )
    }
}

@Preview
@Composable
internal fun HomeScreenContentErrorPreview() {
    Surface {
        HomeScreenContent(
            currentPriceUiState = Resource.Error(RuntimeException("Error")),
            historicalPricesUiState = Resource.Error(RuntimeException("Error")),
            navigateToDetails = {}
        )
    }
}
