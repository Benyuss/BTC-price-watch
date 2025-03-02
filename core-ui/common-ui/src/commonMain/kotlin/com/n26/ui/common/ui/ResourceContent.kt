package com.n26.ui.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.n26.ui.common.model.Resource
import n26bitcoinwatch.core_ui.common_ui.generated.resources.Res
import n26bitcoinwatch.core_ui.common_ui.generated.resources.somethingWentWrong
import org.jetbrains.compose.resources.stringResource

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun <T> ResourceContent(
	resource: Resource<T>,
	loadingContent: @Composable () -> Unit = {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center,
		) {
			CircularProgressIndicator()
		}
	},
	errorContent: @Composable (Throwable) -> Unit = {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = stringResource(Res.string.somethingWentWrong),
			)
		}
	},
	successContent: @Composable (T) -> Unit,
) {
	when (resource) {
		is Resource.Loading -> loadingContent()
		is Resource.Error -> errorContent(resource.throwable)
		is Resource.Success -> successContent(resource.data)
	}
}
