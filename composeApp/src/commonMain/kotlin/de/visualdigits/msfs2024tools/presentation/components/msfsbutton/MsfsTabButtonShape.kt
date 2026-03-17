package de.visualdigits.msfs2024tools.presentation.components.msfsbutton

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class MsfsTabButtonShape(
    val radius: Dp,
    val cutSizeX: Float,
    val cutSizeY: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
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
}
