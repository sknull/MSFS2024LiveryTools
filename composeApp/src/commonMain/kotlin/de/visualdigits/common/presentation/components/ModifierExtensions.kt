package de.visualdigits.common.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.bevelBorder(
    borderSize: Dp = 1.dp,
    borderAlpha: Float = 0.6f,
    shape: Shape,
    inset: Boolean = false
): Modifier {
    val density = LocalDensity.current

    return drawWithCache {
        val widthPx = size.width
        val heightPx = size.height
        val strokeWidth = borderSize.toPx()
        val insetSize = Size(size.width - strokeWidth, size.height - strokeWidth)

        val offsetColors = heightPx / widthPx
        val offsetBorder = widthPx / (widthPx + heightPx)

        val color1 = Color.White.copy(alpha = borderAlpha)
        val color2 = Color.Black.copy(alpha = borderAlpha)
        val (startColor, endColor) = if (inset) {
            Pair(color2, color1)
        } else {
            Pair(color1, color2)
        }

        val brush = Brush.linearGradient(
            colorStops = arrayOf(
                (offsetColors - 0.00f).coerceAtLeast(0.0f) to startColor,
                (offsetColors - 0.15f).coerceAtLeast(0.0f) to startColor,
                (offsetColors - 0.05f).coerceAtLeast(0.0f) to Color.Transparent,
                (offsetColors + 0.05f).coerceAtMost(1.0f) to Color.Transparent,
                (offsetColors + 0.15f).coerceAtMost(1.0f) to endColor,
                (offsetColors + 1.00f).coerceAtMost(1.0f) to endColor,
            ),
            start = Offset(offsetBorder * 100.0f, 0f),
            end = Offset(100.0f, 100.0f)
        )

        onDrawWithContent {
            drawContent()
            drawOutline(
                outline = shape.createOutline(insetSize, layoutDirection, density),
                brush = brush,
                style = Stroke(width = borderSize.toPx())
            )
        }
    }
}
