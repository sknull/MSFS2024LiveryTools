package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TabButtonRow(
    buttonRowModifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    initializeViewModel: () -> Unit = {},
    items: LinkedHashMap<StringResource, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable (StringResource, Int) -> Unit
) {
    initializeViewModel()

    Row(
        modifier = buttonRowModifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, label ->
            button(label, index)
        }
    }
    // outer box
    Box(
        modifier = containerModifier
    ) {
        // inner semitransparent box
        Box(
            modifier = contentModifier,
        ) {
            items.toList()[selectedTab()].second()
        }
    }
}
