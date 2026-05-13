package de.visualdigits.msfs2024tools.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.style.ProjectStyle.BackgroundImageMain
import org.jetbrains.compose.resources.imageResource

@Composable
fun ScreenFrame(
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit
) {

    // blurred background image
    val density = LocalDensity.current
    val backgroundImage = imageResource(BackgroundImageMain)
    val w = with(density) { backgroundImage.width.toDp() }
    val h = with(density) { backgroundImage.height.toDp() }

    Box(
        modifier = modifier
            .paint(
                painter = BitmapPainter(backgroundImage),
                alignment = Alignment.TopStart,
                contentScale = ContentScale.None
            )
            .width(w)
            .height(h)
    )

    // stacked non blurred overlay images and headline texts
    Column(
        modifier = modifier
            .background(Color.Transparent)
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
