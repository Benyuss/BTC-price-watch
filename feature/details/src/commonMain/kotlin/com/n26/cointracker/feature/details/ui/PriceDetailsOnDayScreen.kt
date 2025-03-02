package com.n26.cointracker.feature.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.n26.cointracker.core.domain.model.PriceOnDayDto
import com.n26.cointracker.feature.details.viewmodel.PriceDetailsOnDayViewModel
import com.n26.cointracker.ui.designsystem.DesignSystemTheme
import com.n26.ui.common.model.Resource
import com.n26.ui.common.ui.ResourceContent
import com.n26.ui.common.ui.component.ChildScreenStatusBar
import n26bitcoinwatch.feature.details.generated.resources.Res
import n26bitcoinwatch.feature.details.generated.resources.eur
import n26bitcoinwatch.feature.details.generated.resources.gbp
import n26bitcoinwatch.feature.details.generated.resources.loading
import n26bitcoinwatch.feature.details.generated.resources.usd
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PriceDetailsOnDayScreen(
    dateTimeStamp: Long,
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<PriceDetailsOnDayViewModel>()
    val resource by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.refresh(dateTimeStamp)
        onDispose { }
    }

    PriceDetailsOnDayScreenContent(resource, navigateBack)
}

@Composable
internal fun PriceDetailsOnDayScreenContent(
    resource: Resource<PriceOnDayDto>,
    navigateBack: () -> Unit
) {
    val navigationBarColor = Color.White

    Scaffold(
        modifier = Modifier
            .background(navigationBarColor)
            .systemBarsPadding(),
        topBar = {
            ChildScreenStatusBar(
                onBackButtonClick = navigateBack,
                title = when (resource) {
                    is Resource.Success -> resource.data.date
                    else -> stringResource(Res.string.loading)
                },
                backgroundColor = navigationBarColor,
                titleColor = DesignSystemTheme.colors.brandColor
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(DesignSystemTheme.measurements.spacing4)
        ) {
            ResourceContent(
                resource = resource,
            ) { detailsData ->
                PriceDetailsContent(detailsData)
            }
        }
    }
}

@Composable
private fun PriceDetailsContent(
    details: PriceOnDayDto,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignSystemTheme.measurements.spacing6),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PriceInfoRow(label = stringResource(Res.string.usd), price = details.usd)
        PriceInfoRow(label = stringResource(Res.string.gbp), price = details.gbp)
        PriceInfoRow(label = stringResource(Res.string.eur), price = details.eur)
    }
}

@Composable
private fun PriceInfoRow(label: String, price: Double) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.DarkGray
        )
        Text(
            text = price.toString(),
            style = MaterialTheme.typography.displaySmall,
            color = DesignSystemTheme.colors.brandColor,
            textAlign = TextAlign.End
        )
    }
}
