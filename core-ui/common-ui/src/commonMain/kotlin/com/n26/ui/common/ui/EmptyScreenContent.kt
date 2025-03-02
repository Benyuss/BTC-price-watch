package com.n26.ui.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import n26bitcoinwatch.core_ui.common_ui.generated.resources.Res
import n26bitcoinwatch.core_ui.common_ui.generated.resources.noDataAvailable
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyScreenContent(
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center,
	) {
		Text(stringResource(Res.string.noDataAvailable))
	}
}
