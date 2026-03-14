package de.visualdigits.common.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    title: String,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
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
            .animateContentSize()
            .padding(top = 8.dp),
        value = "",
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        decorationBox = { _ ->
            OutlinedTextFieldDefaults.DecorationBox(
                innerTextField = {
                    Column(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable {
                                isExpanded = !isExpanded
                                onStateChange(isExpanded)
                            }
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isExpanded) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.End),
                                painter = painterResource(Res.drawable.icon_arrow_drop_down_24px),
                                contentDescription = null,
                                tint = Color.White
                            )

                            AnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                content()
                            }
                        } else {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.End),
                                painter = painterResource(Res.drawable.icon_arrow_right_24px),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                },
                visualTransformation = VisualTransformation.None,
                label = { Text(title) },
                value = "",
                singleLine = false,
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = unfocusedBorderColor,
                    focusedBorderColor = focusedBorderColor
                ),
                container = {
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
            )
        }
    )
}
