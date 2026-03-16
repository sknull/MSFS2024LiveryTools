package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
    buttonShape: Shape? = null,
    buttonColor: Color? = null,
    enabled: Boolean = true,
    iconOnly: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    if (toolTip != null) {
        ToolTip(
            text = toolTip,
            modifier = modifier
        ) {
            InnerIcon(
                icon = icon,
                iconTint = iconTint,
                size = size,
                toolTip = toolTip,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                enabled = enabled,
                iconOnly = iconOnly,
                modifier = modifier,
                interactionSource = interactionSource,
                isHovered = isHovered,
                onClick = onClick
            )
        }
    } else {
        InnerIcon(
            icon = icon,
            iconTint = iconTint,
            size = size,
            toolTip = toolTip,
            buttonShape = buttonShape,
            buttonColor = buttonColor,
            enabled = enabled,
            iconOnly = iconOnly,
            modifier = modifier,
            interactionSource = interactionSource,
            isHovered = isHovered,
            onClick = onClick
        )
    }
}

@Composable
fun InnerIcon(
    icon: Painter,
    iconTint: Color,
    size: Dp,
    toolTip: String? = null,
    buttonShape: Shape? = null,
    buttonColor: Color? = null,
    enabled: Boolean = true,
    iconOnly: Boolean = true,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource,
    isHovered: Boolean,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        if (iconOnly) {
            Box(
                modifier = modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .hoverable(interactionSource = interactionSource)
                    .width(width = size)
                    .height(height = size)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable(onClick = onClick),
                    painter = icon,
                    contentDescription = null,
                    tint = if (enabled) if (isHovered) iconTint.copy(alpha = 0.5f) else iconTint else Color(
                        0xff777777
                    ),
                )

            }
        } else {
            FlexibleTextButton(
                width = size,
                height = size,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = onClick,
                enabled = enabled,
                modifier = modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = if (enabled) buttonColor else Color(0xff444444),
                buttonShape = buttonShape ?: RectangleShape,
                leadingIcon = {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = if (enabled) iconTint else Color(0xff777777),
                    )
                }
            )
        }
    } else {
        Icon(
            modifier = modifier,
            painter = icon,
            tint = iconTint,
            contentDescription = toolTip
        )
    }
}
