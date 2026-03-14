package de.visualdigits.common.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlexibleTextButton(
    text: String? = null,
    toolTip: String? = null,
    width: Dp = Dp.Unspecified,
    height: Dp = Dp.Unspecified,
    outerPaddingValues: PaddingValues = PaddingValues(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
    contentAlignment: Alignment = Alignment.Center,
    paddingStart: Dp = 8.dp,
    paddingTop: Dp = 4.dp,
    paddingEnd: Dp = 8.dp,
    paddingBottom: Dp = 4.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color? = null,
    buttonBrush: Brush? = null,
    buttonShape: Shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    hoverColor: Color? = Color.Red,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    when {
        !enabled -> colors.disabledContainerColor
        isHovered && hoverColor != null -> hoverColor
        isHovered -> (buttonColor ?: colors.containerColor).copy(alpha = 0.8f)
        else -> Color.Transparent
    }

    var boxModifier = modifier
        .semantics { role = Role.Button }
        .width(width = width)
        .height(height = height)
        .padding(0.dp)
        .clip(buttonShape)
        .hoverable(interactionSource = interactionSource)
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = onClick
        )

    if (border != null){
        boxModifier = boxModifier
            .border(border)
    }

    if (buttonColor != null){
        boxModifier = boxModifier
            .background(
                color = buttonColor
            )
    }

    if (buttonBrush != null){
        boxModifier = boxModifier
            .background(
                brush = buttonBrush
            )
    }

    ToolTip(toolTip) {
        Box(
            modifier = boxModifier,
            contentAlignment = contentAlignment
        ) {
            Row(
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment,
                modifier = Modifier
                    .padding(outerPaddingValues),
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    if (text != null) Spacer(Modifier.width(8.dp))
                }

                if (text != null) {
                    Text(
                        text = text,
                        color = textColor,
                        modifier = Modifier
                            .padding(start = paddingStart, top = paddingTop, end = paddingEnd, bottom = paddingBottom)
                    )
                }

                if (trailingIcon != null) {
                    if (text != null || leadingIcon != null) Spacer(Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }
    }
}
