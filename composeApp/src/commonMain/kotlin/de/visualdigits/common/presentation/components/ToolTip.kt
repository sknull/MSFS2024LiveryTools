package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolTip(
    text: String?,
    content: @Composable () -> Unit
) {
    if (text != null) {
        TooltipArea(
            tooltip = {
                Surface(
                    modifier = Modifier
                        .shadow(4.dp)
                ) {
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            },
            delayMillis = 600,
            content = content
        )
    } else {
        content()
    }
}
