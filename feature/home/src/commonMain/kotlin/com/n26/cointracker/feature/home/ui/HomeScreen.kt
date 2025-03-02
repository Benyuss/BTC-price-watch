package com.n26.cointracker.feature.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.cointracker.feature.home.viewmodel.CurrentPriceViewModel
import com.n26.cointracker.feature.home.viewmodel.HistoricalPricesViewModel
import com.n26.cointracker.ui.designsystem.DesignSystemTheme
import com.n26.ui.common.model.Resource
import com.n26.ui.common.ui.EmptyScreenContent
import com.n26.ui.common.ui.ResourceContent
import n26bitcoinwatch.feature.home.generated.resources.Res
import n26bitcoinwatch.feature.home.generated.resources.bitcoinPriceTracker
import n26bitcoinwatch.feature.home.generated.resources.currentPriceUsd
import n26bitcoinwatch.feature.home.generated.resources.historicalPrices
import n26bitcoinwatch.feature.home.generated.resources.madeWithLoveByN26
import n26bitcoinwatch.feature.home.generated.resources.usdValue
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
	navigateToDetails: (date: Long) -> Unit,
) {
	val currentPriceViewModel = koinViewModel<CurrentPriceViewModel>()
	val historicalPricesViewModel = koinViewModel<HistoricalPricesViewModel>()

	val currentPriceUiState by currentPriceViewModel.state.collectAsStateWithLifecycle()
	val historicalPricesUiState by historicalPricesViewModel.state.collectAsStateWithLifecycle()

	DisposableEffect(Unit) {
		currentPriceViewModel.refresh()
		historicalPricesViewModel.refresh()
		onDispose { }
	}

	LifecycleResumeEffect(key1 = Unit, lifecycleOwner = LocalLifecycleOwner.current) {
		currentPriceViewModel.startAutoRefresh()
		onPauseOrDispose {
			currentPriceViewModel.stopAutoRefresh()
		}
	}

	HomeScreenContent(
		currentPriceUiState = currentPriceUiState,
		historicalPricesUiState = historicalPricesUiState,
		navigateToDetails = navigateToDetails,
	)
}

@Composable
internal fun HomeScreenContent(
	currentPriceUiState: Resource<Double>,
	historicalPricesUiState: Resource<List<HistoricalPriceDto>>,
	navigateToDetails: (date: Long) -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
			.padding(DesignSystemTheme.measurements.spacing4),
	) {
		Text(
			text = stringResource(Res.string.bitcoinPriceTracker),
			style = MaterialTheme.typography.titleLarge,
			color = DesignSystemTheme.colors.brandColor,
		)
		Text(
			text = stringResource(Res.string.madeWithLoveByN26),
			style = MaterialTheme.typography.labelLarge,
			color = Color.Gray,
		)
		Spacer(modifier = Modifier.height(DesignSystemTheme.measurements.spacing6))

		CurrentPriceComponent(currentPriceUiState)

		Spacer(modifier = Modifier.height(DesignSystemTheme.measurements.spacing6))
		HistoricalPricesComponent(historicalPricesUiState, navigateToDetails)
	}
}

@Composable
private fun CurrentPriceComponent(
	currentPriceUiState: Resource<Double>,
) {
	ResourceContent(
		resource = currentPriceUiState,
		loadingContent = {
			Box(
				modifier = Modifier.fillMaxWidth().padding(DesignSystemTheme.measurements.spacing6),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator()
			}
		},
	) { price ->
		AnimatedContent(targetState = price) { animatedPrice ->
			Text(
				text = stringResource(Res.string.usdValue, animatedPrice),
				style = MaterialTheme.typography.displayLarge,
				color = DesignSystemTheme.colors.brandColor,
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = DesignSystemTheme.measurements.spacing1),
				textAlign = TextAlign.Center,
			)
		}

		Text(
			text = stringResource(Res.string.currentPriceUsd),
			style = MaterialTheme.typography.labelLarge,
			color = Color.Gray,
			textAlign = TextAlign.Center,
			modifier = Modifier.fillMaxWidth(),
		)
	}
}

@Composable
private fun HistoricalPricesComponent(
	historicalPricesUiState: Resource<List<HistoricalPriceDto>>,
	navigateToDetails: (date: Long) -> Unit,
) {
	Column {
		Text(
			text = stringResource(Res.string.historicalPrices),
			style = MaterialTheme.typography.titleMedium,
			color = Color.DarkGray,
			modifier = Modifier.align(Alignment.Start),
		)

		ResourceContent(resource = historicalPricesUiState) { prices ->
			if (prices.isNotEmpty()) {
				LazyColumn(
					contentPadding = PaddingValues(vertical = DesignSystemTheme.measurements.spacing2),
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.spacedBy(DesignSystemTheme.measurements.spacing3),
				) {
					items(prices, key = { it.utcTimestamp }) { priceDto ->
						HistoricalPriceItem(
							priceDto = priceDto,
							onClick = { navigateToDetails(priceDto.utcTimestamp) },
						)
					}
				}
			} else {
				EmptyScreenContent(Modifier.fillMaxSize())
			}
		}
	}
}

@Composable
private fun HistoricalPriceItem(
	priceDto: HistoricalPriceDto,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.clickable { onClick() },
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		colors = CardColors(
			containerColor = DesignSystemTheme.colors.secondaryColor,
			contentColor = Color.Unspecified,
			disabledContainerColor = Color.Unspecified,
			disabledContentColor = Color.Unspecified,
		),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.clickable { onClick() }
				.padding(vertical = DesignSystemTheme.measurements.spacing4),
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = DesignSystemTheme.measurements.spacing4),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = priceDto.localDate.toString(),
						style = MaterialTheme.typography.bodySmall,
						color = Color.DarkGray,
					)
					Spacer(modifier = Modifier.height(DesignSystemTheme.measurements.spacing1))
					Text(
						text = stringResource(Res.string.usdValue, priceDto.price),
						style = MaterialTheme.typography.bodyLarge,
						color = DesignSystemTheme.colors.brandColor,
					)
				}
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowForward,
					contentDescription = null,
					tint = Color.Gray,
				)
			}
		}
	}
}
