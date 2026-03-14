package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SquaredIconButton(
    icon: Painter,
    iconTint: Color,
    size: Dp,
    toolTip: String? = null,
    buttonShape: Shape,
    buttonColor: Color,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    if (toolTip != null) {
        ToolTip(text = toolTip) {
            if (onClick != null) {
                InnerButton(
                    icon = icon,
                    iconTint = iconTint,
                    enabled = enabled,
                    onClick = onClick,
                    buttonShape = buttonShape,
                    size = size,
                    buttonColor = buttonColor
                )
            } else {
                Icon(
                    painter = icon,
                    contentDescription = toolTip
                )
            }
        }
    } else {
        if (onClick != null) {
            InnerButton(
                icon = icon,
                iconTint = iconTint,
                onClick = onClick,
                enabled = enabled,
                buttonShape = buttonShape,
                size = size,
                buttonColor = buttonColor
            )
        } else {
            Icon(
                painter = icon,
                contentDescription = toolTip
            )
        }
    }
}

@Composable
private fun InnerButton(
    icon: Painter,
    iconTint: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    buttonShape: Shape,
    size: Dp,
    buttonColor: Color
) {
    FlexibleTextButton(
        width = size,
        height = size,
        paddingStart = 0.dp,
        paddingTop = 0.dp,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(end = 5.dp),
        buttonColor = if (enabled) buttonColor else Color(0xff444444),
        buttonShape = buttonShape,
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (enabled) iconTint else Color(0xff777777),
            )
        }
    )
}
