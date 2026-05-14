package de.visualdigits.msfs2024tools.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenFrame(
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit
) {

    // stacked non blurred overlay images and headline texts
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // page header
        Text(
            text = "MSFS 2024 Livery Tools",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 100.dp, top = 30.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(start = 100.dp, top = 10.dp, bottom = 30.dp)
        )

        // outer box
        Box(
            modifier = containerModifier
        ) {
            // inner semitransparent box
            Box(
                modifier = contentModifier,
            ) {
                content()
            }
        }
    }
}
