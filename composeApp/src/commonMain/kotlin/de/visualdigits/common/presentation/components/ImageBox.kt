package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource

@Composable
fun ImageBox(
    image: DrawableResource,
    width: Dp? = null,
    height: Dp? = null,
    contentScale: ContentScale = ContentScale.None,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val backgroundImage = imageResource(image)
    val w = width?:with(density) { backgroundImage.width.toDp() }
    val h = height?:with(density) { backgroundImage.height.toDp() }

    Box(
        modifier = modifier
            .paint(
                painter = BitmapPainter(backgroundImage),
                alignment = Alignment.TopStart,
                contentScale = contentScale
            )
            .width(w)
            .height(h)
    )
}
