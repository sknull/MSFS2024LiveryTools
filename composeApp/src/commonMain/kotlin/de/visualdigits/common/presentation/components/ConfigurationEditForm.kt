package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.color
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.cancel
import msfs2024liverytools.composeapp.generated.resources.icon_cancel_24px
import msfs2024liverytools.composeapp.generated.resources.icon_check_small_24px
import msfs2024liverytools.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationEditForm(
    modifier: Modifier = Modifier,
    fieldHeight: Dp = Dp.Unspecified,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonShape: Shape,
    buttonColor: Color,
    containerShape: Shape,
    space: Dp,
    onValueChange: (KeyValue) -> Unit,
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    deleteAllowed: (String, String) -> Boolean = { _,_ -> true },
    configuration: AbstractConfiguration<*>?,
    state: Msfs2024ToolsState
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        configuration
            ?.fields
            ?.filter { (key, field) -> field.descriptor.visible }
            ?.values
            ?.chunked(2)
            ?.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space)
                ) {
                    rowItems.forEach { field ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            EditableListField(
                                field = field,
                                fieldHeight = fieldHeight,
                                space = space,
                                unfocusedBorderColor = unfocusedBorderColor,
                                focusedBorderColor = focusedBorderColor,
                                iconTint = iconTint,
                                buttonColor = buttonColor,
                                buttonShape = buttonShape,
                                containerShape = containerShape,
                                onValueChange = onValueChange,
                                deleteAllowed = deleteAllowed
                            )
                        }
                    }

                    repeat(2 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        Row(
            horizontalArrangement = Arrangement.spacedBy(space),
            modifier = Modifier
                .align(Alignment.End)
                .wrapContentWidth()
        ) {
            FlexibleTextButton(
                text = stringResource(Res.string.cancel),
                height = 30.dp,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = onCancelClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = buttonColor,
                buttonShape = buttonShape,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_cancel_24px),
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            )

            FlexibleTextButton(
                text = stringResource(Res.string.ok),
                enabled = !state.isLoading,
                height = 30.dp,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = onOkClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = buttonColor,
                buttonShape = buttonShape,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_check_small_24px),
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            )
        }
    }
}

@Composable
private fun EditableListField(
    field: Field<*,*,*>,
    fieldHeight: Dp,
    space: Dp,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonColor: Color,
    buttonShape: Shape,
    containerShape: Shape,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (String, String) -> Boolean
) {
    val isEditable = !field.descriptor.readOnly
    if (field.valid()) Color.Unspecified else Severity.Error.color()

    when(field.descriptor) {
        is ListFieldDescriptor<*> -> {
            EditableList(
                field = field as Field<ListFieldDescriptor<Any>, MutableList<Any>, Any>,
                fieldHeight = fieldHeight,
                space = space,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                iconTint = iconTint,
                buttonColor = buttonColor,
                buttonShape = buttonShape,
                containerShape = containerShape,
                onValueChange = onValueChange,
                valid = {
                    field.valid()
                },
                deleteAllowed = deleteAllowed
            )
        }

        else -> {
            TypeAwareEditableField(
                modifier = Modifier
                    .fillMaxWidth(),
                field = field,
                height = fieldHeight,
                enabled = isEditable,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                iconTint = iconTint,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                onValueChange = onValueChange,
                valid = {
                    field.valid()
                }
            )
        }
    }
}

