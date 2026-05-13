package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.button.FlexibleTextButton
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.add
import de.visualdigits.compose.resources.add_hint
import de.visualdigits.compose.resources.cancel
import de.visualdigits.compose.resources.delete
import de.visualdigits.compose.resources.edit
import de.visualdigits.compose.resources.icon_add_24px
import de.visualdigits.compose.resources.icon_cancel_24px
import de.visualdigits.compose.resources.icon_delete_24px
import de.visualdigits.compose.resources.icon_edit_24px
import de.visualdigits.compose.resources.icon_file_save_24px
import de.visualdigits.compose.resources.icon_folder_open_24px
import de.visualdigits.compose.resources.ok
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> EditableListStandalone(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    fieldHeight: Dp = Dp.Unspecified,
    space: Dp,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonShape: Shape,
    containerShape: Shape,
    buttonColor: Color,
    scrollable: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>, String) -> Boolean = { _, _ -> true }
) {
    val interactionSource = remember { MutableInteractionSource() }

    val values = fieldState.fieldDescriptor.options(fieldState.configuration, null).map { triple -> triple.first.toString() }
    val previousItems = remember(values) { values.toMutableStateList() }
    val items = remember(values) { values.toMutableStateList() }
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var currentText by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, unfocusedBorderColor, buttonShape)
    ) {
        val scrollState = rememberScrollState(0)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(space)
        ) {
            Text(
                modifier = Modifier,
                text = fieldState.fieldDescriptor.label.asString(),
                style = MaterialTheme.typography.bodySmall,
            )

            items.forEachIndexed { index, item ->
                val allowDelete = deleteAllowed(fieldState.fieldDescriptor, item)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight * 0.75f),
                    shape = buttonShape,
                    color = Color.Transparent,
                    border = BorderStroke(
                        width = 1.dp,
                        color = unfocusedBorderColor
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(start = 8.dp, end = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = item.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )

                        if (fieldState.fieldDescriptor.enabled) {
                            IndicatorButton(
                                leadingIcon = painterResource(Res.drawable.icon_edit_24px),
                                leadingIconTint = iconTint,
                                modifier = Modifier.padding(start = 5.dp),
                                width = 30.dp,
                                height = 30.dp,
                                toolTip = stringResource(Res.string.edit),
                                shape = buttonShape,
                                buttonColor = buttonColor,
                                onClick = {
                                    editingIndex = index
                                    currentText = item.toString()
                                    showDialog = true
                                }
                            )

                            IndicatorButton(
                                leadingIcon = painterResource(Res.drawable.icon_delete_24px),
                                leadingIconTint = iconTint,
                                modifier = Modifier.padding(start = 5.dp),
                                width = 30.dp,
                                height = 30.dp,
                                toolTip = stringResource(Res.string.delete),
                                enabled = allowDelete,
                                shape = buttonShape,
                                buttonColor = buttonColor,
                                onClick = {
                                    editingIndex = null
                                    currentText = null
                                    items.removeAt(index)
                                    showDialog = false
                                    onValueChange(KeyValue(fieldState.fieldDescriptor, items.joinToString(",")))
                                }
                            )
                        }
                    }
                }
            }

            if (fieldState.fieldDescriptor.enabled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    FlexibleTextButton(
                        text = stringResource(Res.string.add_hint),
                        height = 30.dp,
                        paddingStart = 0.dp,
                        paddingTop = 0.dp,
                        onClick = {
                            editingIndex = null
                            currentText = ""
                            showDialog = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .pointerHoverIcon(PointerIcon.Hand),
                        buttonColor = buttonColor,
                        buttonShape = buttonShape,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.icon_add_24px),
                                contentDescription = stringResource(Res.string.add_hint),
                                tint = iconTint
                            )
                        },
                    )
                }
            }
        }

        if (scrollable) {
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState = scrollState),
                interactionSource = interactionSource,
                modifier = Modifier
                    .clip(containerShape)
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .width(8.dp),
                style = defaultScrollbarStyle().copy(
                    unhoverColor = Color.White.copy(alpha = 0.6f),
                    hoverColor = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }

    if (showDialog) {
        previousItems.update(items)

        AlertDialog(
            modifier = Modifier
                .border(1.dp, focusedBorderColor, containerShape),
            containerColor = Color.Black.copy(alpha = 0.6f),
            shape = containerShape,
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingIndex == null) stringResource(Res.string.add) else stringResource(Res.string.edit)) },
            text = {
                TypeAwareEditableField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    fieldState = fieldState,
                    currentValue = currentText,
                    fieldHeight = fieldHeight,
                    unfocusedBorderColor = unfocusedBorderColor,
                    focusedBorderColor = focusedBorderColor,
                    iconTint = iconTint,
                    buttonShape = buttonShape,
                    buttonColor = buttonColor,
                    onValueChange = { keyValue ->
                        currentText = keyValue.value?.toString() ?: ""
                    },
                    titleChooseDirectory = UiText.DynamicString(""),
                    titleChooseFile = UiText.DynamicString(""),
                    iconFolder = painterResource(Res.drawable.icon_folder_open_24px),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                FlexibleTextButton(
                    text = stringResource(Res.string.ok),
                    height = 30.dp,
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        val previousValue = editingIndex?.let { i -> items[i] }
                        if (editingIndex != null) {
                            currentText?.also { ct -> items[editingIndex!!] = ct }

                        } else {
                            currentText?.also { ct -> items.add(ct) }
                        }
                        onValueChange(
                            KeyValue(
                                descriptor = fieldState.fieldDescriptor,
                                value = items.joinToString(","),
                                previousValue = previousValue,
                                newValue = currentText
                            )
                        )
                        showDialog = false
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    buttonColor = buttonColor,
                    buttonShape = buttonShape,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_file_save_24px),
                            contentDescription = null,
                            tint = iconTint
                        )
                    },
                )
            },
            dismissButton = {
                FlexibleTextButton(
                    text = stringResource(Res.string.cancel),
                    height = 30.dp,
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        items.update(previousItems)
                        onValueChange(KeyValue(fieldState.fieldDescriptor, items.joinToString(",")))
                        showDialog = false
                    },
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
                    },
                )
            }
        )
    }
}

private fun <T> SnapshotStateList<T>.update(values: MutableList<T>) {
    clear()
    addAll(values)
}
