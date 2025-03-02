package com.n26.ui.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import n26bitcoinwatch.core_ui.common_ui.generated.resources.Res
import n26bitcoinwatch.core_ui.common_ui.generated.resources.back
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChildScreenStatusBar(
    onBackButtonClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    icon: StatusBarIcon? = StatusBarIcon.Back,
    rightIcon: ImageVector? = null,
    backgroundColor: Color = Color.White,
    componentPaddingTop: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 8.dp,
    iconPadding: Dp = 8.dp,
    iconSize: Dp = 24.dp,
    iconColor: Color = Color.Gray,
    titleColor: Color = Color.Gray,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titlePadding: Dp = 8.dp,
    topBarDividerColor: Color = Color.LightGray,
    onRightIconClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = componentPaddingTop),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = verticalPadding,
                    horizontal = horizontalPadding,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val measuredIconSize = iconPadding.plus(iconSize)

            icon?.let {
                val actionIcon = when (icon) {
                    StatusBarIcon.Back -> Icons.AutoMirrored.Filled.ArrowBack
                    StatusBarIcon.Close -> Icons.Filled.Close
                }
                Icon(
                    modifier = Modifier
                        .size(measuredIconSize)
                        .clickable {
                            onBackButtonClick()
                        },
                    imageVector = actionIcon,
                    contentDescription = stringResource(Res.string.back),
                    tint = iconColor,
                )
            }

            rightIcon?.let {
                Spacer(modifier = Modifier.size(measuredIconSize))
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = titlePadding),
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = titleTextStyle,
                textAlign = TextAlign.Center,
                color = titleColor,
            )
            icon?.let {
                Spacer(modifier = Modifier.size(measuredIconSize))
            }

            rightIcon?.let {
                Icon(
                    modifier = Modifier
                        .size(measuredIconSize)
                        .clickable {
                            onRightIconClick?.invoke()
                        },
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = iconColor,
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = topBarDividerColor,
        )
    }
}

enum class StatusBarIcon {
    Back,
    Close,
}
