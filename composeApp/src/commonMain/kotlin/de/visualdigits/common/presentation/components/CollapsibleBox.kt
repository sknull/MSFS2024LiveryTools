package de.visualdigits.common.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_arrow_drop_down_24px
import msfs2024liverytools.composeapp.generated.resources.icon_arrow_right_24px
import org.jetbrains.compose.resources.painterResource

@Composable
fun CollapsibleBox(
    modifier: Modifier = Modifier,
    title: String?,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    backgroundColor: Color,
    buttonShape: Shape,
    onStateChange: (Boolean) -> Unit = { },
    isExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isExpanded by remember { mutableStateOf(isExpanded) }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clip(buttonShape)
            .background(backgroundColor)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        value = "",
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        decorationBox = { _ ->
            OutlinedTextFieldDefaults.DecorationBox(
                innerTextField = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(buttonShape)
                            .padding(8.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable {
                                isExpanded = !isExpanded
                                onStateChange(isExpanded)
                            },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        ) {
                            title?.let { t ->
                                Text(
                                    text = t,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            if (isExpanded) {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_arrow_drop_down_24px),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            } else {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_arrow_right_24px),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        if (isExpanded) {
                            content()
                        }
                    }
                },
                visualTransformation = VisualTransformation.None,
                value = "",
                singleLine = false,
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = unfocusedBorderColor,
                    focusedBorderColor = focusedBorderColor,
                ),
                contentPadding = PaddingValues(top = 8.dp, end = 0.dp, bottom = 0.dp, start = 0.dp)
            ) {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = unfocusedBorderColor,
                        focusedBorderColor = focusedBorderColor
                    ),
                    shape = buttonShape,
                )
            }
        }
    )
}
