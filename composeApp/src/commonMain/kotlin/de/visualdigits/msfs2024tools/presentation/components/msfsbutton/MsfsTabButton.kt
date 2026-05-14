package de.visualdigits.msfs2024tools.presentation.components.msfsbutton

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_checkmark
import org.jetbrains.compose.resources.painterResource


@Composable
fun MsfsTabButton(
    text: String,
    width: Dp = 160.dp,
    height: Dp = 50.dp,
    paddingStart: Dp = 8.dp,
    paddingTop: Dp = 5.dp,
    paddingEnd: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .semantics { role = Role.Button }
            .width(width = width)
            .height(height = height)
            .padding(0.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .hoverable(interactionSource = interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .msfs2024Button(
                selected = selected,
                selectedBgColor = Color(0xff193866)
            ),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            modifier = Modifier
                .padding(start = paddingStart, top = paddingTop, end = paddingEnd, bottom = paddingBottom),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}

@Composable
private fun Modifier.msfs2024Button(
    selected: Boolean,
    selectedBgColor: Color
): Modifier {

    val painter = painterResource(Res.drawable.icon_checkmark)
    val imageSize = painter.intrinsicSize
    val unselectedBrush = Brush.linearGradient(listOf(Color(0xff2e68ba), Color(0xff133c7b)))

    return drawWithCache {
        val widthPx = size.width
        val heightPx = size.height

        onDrawWithContent {

            if (selected) {
                drawRect(
                    color = selectedBgColor
                )
                withTransform({
                    translate(left = widthPx - imageSize.width - 2, top = heightPx - imageSize.height - 2)
                }) {
                    painter.apply {
                        draw(size = imageSize)
                    }
                }

                drawOutline(
                    outline = createMsfsButtonOutline(
                        radius = 6.0.dp,
                        cutSizeX = 0.3f,
                        cutSizeY = 0.6f,
                        size = size,
                        density = this@drawWithCache
                    ),
                    color = Color(0xff3b84eb),
                )
            } else {
                drawRect(
                    brush = unselectedBrush
                )
            }

            drawContent()

        }
    }
}


private fun createMsfsButtonOutline(
    radius: Dp,
    cutSizeX: Float,
    cutSizeY: Float,
    size: Size,
    density: Density
): Outline {
    val radiusF = with(density) { radius.toPx() }
    val path = Path().apply {
        // 1. Oben links starten (nach der Rundung)
        moveTo(radiusF, 0f)

        // 2. Oben rechts abrunden
        lineTo(size.width - radiusF, 0f)
        arcTo(
            rect = Rect(Offset(size.width - 2 * radiusF, 0f), Size(2 * radiusF, 2 * radiusF)),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        // 3. Unten rechts: Der diagonale Schnitt (CutCorner)
        lineTo(size.width, size.height - cutSizeY * size.height)
        lineTo(size.width - cutSizeX * size.width, size.height)

        // 4. Unten links abrunden
        lineTo(radiusF, size.height)
        arcTo(
            rect = Rect(Offset(0f, size.height - 2 * radiusF), Size(2 * radiusF, 2 * radiusF)),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        // 5. Zurück nach oben links abrunden
        lineTo(0f, radiusF)
        arcTo(
            rect = Rect(Offset.Companion.Zero, Size(2 * radiusF, 2 * radiusF)),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        close()
    }

    return Outline.Generic(path)
}
