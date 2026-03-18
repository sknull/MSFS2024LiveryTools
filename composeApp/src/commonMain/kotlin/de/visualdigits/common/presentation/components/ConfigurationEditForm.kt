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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.Configuration
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.color
import de.visualdigits.common.domain.model.fieldValues
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.cancel
import msfs2024liverytools.composeapp.generated.resources.icon_cancel_24px
import msfs2024liverytools.composeapp.generated.resources.icon_check_small_24px
import msfs2024liverytools.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
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
    configuration: Configuration<*>?,
    state: Msfs2024ToolsState
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        configuration
            ?.asMap()
            ?.toList()
            ?.chunked(2)
            ?.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space)
                ) {
                    rowItems.forEach { (key, value) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            EditableListField(
                                configuration = configuration,
                                key = key,
                                fieldHeight = fieldHeight,
                                space = space,
                                unfocusedBorderColor = unfocusedBorderColor,
                                focusedBorderColor = focusedBorderColor,
                                iconTint = iconTint,
                                buttonColor = buttonColor,
                                buttonShape = buttonShape,
                                containerShape = containerShape,
                                value = value,
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
    configuration: Configuration<*>?,
    key: String,
    fieldHeight: Dp,
    space: Dp,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonColor: Color,
    buttonShape: Shape,
    containerShape: Shape,
    value: String?,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (String, String) -> Boolean
) {
    val label = configuration?.labelResource(key)?.let { sr -> stringResource(sr) } ?: key
    val toolTip = configuration?.toolTipResource(key)?.let { sr -> stringResource(sr) }
    val startDirectory = configuration?.startDirectory(key)
    val clazz = configuration?.fieldClass(key)
    val fieldClass = clazz?.first ?: Any::class.java
    val collectionClass = clazz?.second ?: Any::class.java
    val isEditable = configuration?.fieldIsEditable(key) ?: true
    if (configuration?.valid(key) == true) Color.Unspecified else Severity.Error.color()
    val options = when {
        StringResourceEnumerable::class.java.isAssignableFrom(fieldClass) -> {
            configuration
                ?.fieldValues<StringResource, DrawableResource>(key)
                ?.mapNotNull { triple -> triple?.let { t -> Triple(t.first, stringResource(t.second), t.third?.let { d -> painterResource(d) }) }  }
                ?: listOf()
        }

        else -> {
            configuration
                ?.fieldValues<String, Painter>(key)
                ?.mapNotNull { e -> e }
                ?: listOf()
        }
    }

    when {
        List::class.java.isAssignableFrom(collectionClass) -> {
            EditableList(
                fieldHeight = fieldHeight,
                space = space,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                iconTint = iconTint,
                buttonColor = buttonColor,
                buttonShape = buttonShape,
                containerShape = containerShape,
                key = key,
                label = label,
                clazz = fieldClass,
                fileMode = configuration?.fileMode(key),
                startDirectory = startDirectory,
                options = options,
                values = if (value?.isNotEmpty() == true) value.split(",").map { v -> v.trim() } else listOf(),
                onValueChange = onValueChange,
                enabled = isEditable,
                valid = {
                    configuration?.valid(key)
                },
                deleteAllowed = deleteAllowed
            )
        }

        else -> {
            TypeAwareEditableField(
                modifier = Modifier
                    .fillMaxWidth(),
                height = fieldHeight,
                clazz = fieldClass,
                fileMode = configuration?.fileMode(key),
                startDirectory = startDirectory,
                options = options,
                key = key,
                label = label,
                toolTip = toolTip,
                value = value,
                enabled = isEditable,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                iconTint = iconTint,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                onValueChange = onValueChange,
                valid = {
                    configuration?.valid(key)
                }
            )
        }
    }
}

