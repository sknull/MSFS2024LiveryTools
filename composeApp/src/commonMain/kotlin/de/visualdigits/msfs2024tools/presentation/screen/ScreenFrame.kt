package de.visualdigits.msfs2024tools.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.ImageBox
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrows
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsScale
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsTranslationX
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsTranslationy
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundImageMain
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScreenFrame(
    modifier: Modifier = Modifier,
    state: Msfs2024ToolsState,
    containerModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    hazeState: HazeState,
    label: String,
    content: @Composable () -> Unit
) {

    // blurred background image
    ImageBox(
        image = BackgroundImageMain,
        modifier = modifier
            .hazeSource(state = hazeState, zIndex = 0f)
            .fillMaxSize()
    )

    // stacked non blurred overlay images and headline texts
    Column(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .hazeSource(state = hazeState, zIndex = 1f)
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    blurRadius = 30.dp,
                    backgroundColor = Color.Unspecified,
                    tint = HazeTint(Color.Black.copy(alpha = 0.2f))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // page header
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(140.dp),
        ) {
            Image(
                painter = painterResource(BackgroundArrows),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Transparent)
                    .scale(BackgroundArrowsScale)
                    .graphicsLayer {
                        translationX = size.width * BackgroundArrowsTranslationX
                        translationY = size.height * BackgroundArrowsTranslationy
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 90.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "MSFS 2024 Livery Tools",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(0.dp)
                    )

                    Text(
                        text = label,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(0.dp)
                    )
                }
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
                content()
            }
        }
    }
}
