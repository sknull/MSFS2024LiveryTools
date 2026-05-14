package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.color
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.form.ConfigurationPanelResources
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.button.IndicatorButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.awt.Desktop
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> ConfigurationPanel(
    configuration: AbstractConfiguration<*,*>?,
    configurationPanelResources: ConfigurationPanelResources = ConfigurationPanelResources.DEFAULT_RESOURCES,
    onEditClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onOkClick: () -> Unit,
    iconTint: Color,
    space: Dp,
    showOkButton: Boolean = false,
    buttonColor: Color,
    shape: Shape,
    focusedBorderColor: Color,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val buttonScope = rememberCoroutineScope()
    configuration as AbstractConfiguration<*, K>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.extraSmall)
            .background(Color.Black.copy(alpha = 0.4f), MaterialTheme.shapes.extraSmall)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space),
        ) {
            IndicatorButton(
                text = configurationPanelResources.label_edit.asString(),
                height = 30.dp,
                padding = 0.dp,
                onClick = onEditClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = buttonColor,
                shape = shape,
                leadingIcon =configurationPanelResources.icon_edit?.let { r -> painterResource(r) }
            )

            if (onDeleteClick != null) {
                IndicatorButton(
                    text = configurationPanelResources.label_delete.asString(),
                    height = 30.dp,
                    padding = 0.dp,
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    buttonColor = buttonColor,
                    shape = shape,
                    leadingIcon = configurationPanelResources.icon_delete?.let { r -> painterResource(r) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            configuration?.fieldDescriptors
                ?.filter { descriptor ->
                    descriptor.visible && descriptor !is SpacerFieldDescriptor<*,*>
                }
                ?.forEach { descriptor ->
                    val value = configuration.values[descriptor.key]
                    item(key = descriptor.key) {
                        Row(
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth()
//                        .drawBehind {
//                            val strokeWidth = 1.dp.toPx()
//                            val y = size.height - strokeWidth / 2
//                            drawLine(
//                                color = Color.Black,
//                                start = Offset(0f, y),
//                                end = Offset(size.width, y),
//                                strokeWidth = strokeWidth
//                            )
//                        },
                        ) {
                            val color = if (descriptor.valid(configuration, value)) {
                                if (value != null) {
                                    Color.Unspecified
                                } else {
                                    Severity.Warn.color()
                                }
                            } else {
                                Severity.Error.color()
                            }

                            Text(
                                text = descriptor.label.asString(),
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillParentMaxWidth(0.3f)
                            )

                            Text(
                                text = descriptor.keyFactory.stringValue(value) ?: configurationPanelResources.placeholder_field_unset.asString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = color,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillParentMaxWidth(0.6f)
                            )

                            if (descriptor.readOnly) {
                                IndicatorButton(
                                    leadingIcon = configurationPanelResources.icon_info?.let { r -> painterResource(r) },
                                    leadingIconTint = iconTint,
                                    modifier = Modifier.padding(start = 5.dp),
                                    width = 30.dp,
                                    height = 30.dp,
                                    toolTip = configurationPanelResources.tooltip_readonly.asString(),
                                    shape = shape,
                                    buttonColor = buttonColor
                                )
                            }

                            if (descriptor is FileFieldDescriptor<*,*> && descriptor.fileMode == FileMode.DIRECTORIES_ONLY) {
                                IndicatorButton(
                                    leadingIcon = configurationPanelResources.icon_folder?.let { r -> painterResource(r) },
                                    leadingIconTint = iconTint,
                                    modifier = Modifier.padding(start = 5.dp),
                                    width = 30.dp,
                                    height = 30.dp,
                                    toolTip = configurationPanelResources.tooltip_open_in_explorer.asString(),
                                    shape = shape,
                                    buttonColor = buttonColor,
                                    enabled = (value as? File)?.exists() == true,
                                    onClick = {
                                        buttonScope.launch(Dispatchers.IO) {
                                            Desktop.getDesktop().open(value as? File)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
        }

        if (showOkButton) {
            IndicatorButton(
                text = configurationPanelResources.label_ok.asString(),
                height = 30.dp,
                padding = 0.dp,
                onClick = onOkClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .align(Alignment.End),
                buttonColor = buttonColor,
                shape = shape,
                leadingIcon = configurationPanelResources.icon_ok?.let { r -> painterResource(r) }
            )
        }
    }

    if (showDeleteDialog && onDeleteClick != null) {
        AlertDialog(
            modifier = Modifier
                .border(1.dp, focusedBorderColor),
            containerColor = Severity.Error.color().copy(alpha = 0.8f),
            shape = shape,
            onDismissRequest = { showDeleteDialog = false },
            title = { 
                Text(
                text = configurationPanelResources.label_delete.asString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space),
                ) {
                    configurationPanelResources.icon_warning?.let { r -> painterResource(r) }?.let {
                        Icon(
                            painter = it,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(100.dp)
                                .aspectRatio(1.0f)
                        )
                    }

                    Text(
                        text = configurationPanelResources.warning_delete.asString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            },
            confirmButton = {
                IndicatorButton(
                    text = configurationPanelResources.label_ok.asString(),
                    height = 30.dp,
                    padding = 0.dp,
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    buttonColor = buttonColor,
                    shape = shape,
                    leadingIcon = configurationPanelResources.icon_save?.let { r -> painterResource(r) },
                )
            },
            dismissButton = {
                IndicatorButton(
                    text = configurationPanelResources.label_cancel.asString(),
                    height = 30.dp,
                    padding = 0.dp,
                    onClick = {
                        showDeleteDialog = false
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    buttonColor = buttonColor,
                    shape = shape,
                    leadingIcon = configurationPanelResources.icon_cancel?.let { r -> painterResource(r) },
                )
            }
        )
    }
}
