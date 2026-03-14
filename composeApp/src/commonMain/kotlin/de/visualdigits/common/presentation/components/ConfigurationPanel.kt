package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.Configuration
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.cancel
import msfs2024liverytools.composeapp.generated.resources.delete
import msfs2024liverytools.composeapp.generated.resources.edit_hint
import msfs2024liverytools.composeapp.generated.resources.field_unset
import msfs2024liverytools.composeapp.generated.resources.icon_cancel_24px
import msfs2024liverytools.composeapp.generated.resources.icon_check_small_24px
import msfs2024liverytools.composeapp.generated.resources.icon_delete_24px
import msfs2024liverytools.composeapp.generated.resources.icon_edit_24px
import msfs2024liverytools.composeapp.generated.resources.icon_file_save_24px
import msfs2024liverytools.composeapp.generated.resources.icon_folder_open_24px
import msfs2024liverytools.composeapp.generated.resources.icon_info_24px
import msfs2024liverytools.composeapp.generated.resources.icon_warning_24px
import msfs2024liverytools.composeapp.generated.resources.ok
import msfs2024liverytools.composeapp.generated.resources.title_delete
import msfs2024liverytools.composeapp.generated.resources.tooltip_openInExplorer
import msfs2024liverytools.composeapp.generated.resources.tooltip_readonly
import msfs2024liverytools.composeapp.generated.resources.warning_delete
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Desktop
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfigurationPanel(
    configuration: Configuration<*>?,
    onEditClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onOkClick: () -> Unit,
    iconTint: Color,
    space: Dp,
    showOkButton: Boolean = false,
    buttonColor: Color,
    buttonShape: Shape,
    focusedBorderColor: Color,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val buttonScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space),
        ) {
            FlexibleTextButton(
                text = stringResource(Res.string.edit_hint),
                height = 30.dp,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = onEditClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = buttonColor,
                buttonShape = buttonShape,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_edit_24px),
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            )

            if (onDeleteClick != null) {
                FlexibleTextButton(
                    text = stringResource(Res.string.delete),
                    height = 30.dp,
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    buttonColor = buttonColor,
                    buttonShape = buttonShape,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_delete_24px),
                            contentDescription = null,
                            tint = iconTint
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = configuration?.asMap()?.toList() ?: listOf(),
                key = { (key, _) -> key }
            ) { (key, value) ->
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
                    val color = if (configuration?.valid(key) == true) {
                        if (value != null) {
                            Color.Unspecified
                        } else {
                            Severity.Warn.color()
                        }
                    } else {
                        Severity.Error.color()
                    }
                    val label = configuration?.labelResource(key)?.let { sr -> stringResource(sr) } ?: key
                    val unset = stringResource(Res.string.field_unset)
                    val clazz = configuration?.fieldClass(key)
                    val fileMode = configuration?.fileMode(key)
                    val isEditable = configuration?.fieldIsEditable(key)?:true

                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillParentMaxWidth(0.3f)
                    )

                    Text(
                        text = value?:unset,
                        style = MaterialTheme.typography.bodySmall,
                        color = color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillParentMaxWidth(0.67f)
                    )

                    if (!isEditable) {
                        SquaredIconButton(
                            icon = painterResource(Res.drawable.icon_info_24px),
                            iconTint = iconTint,
                            size = 30.dp,
                            toolTip = stringResource(Res.string.tooltip_readonly),
                            buttonShape = buttonShape,
                            buttonColor = buttonColor
                        )
                    }

                    if (File::class.java == clazz?.first && fileMode == FileMode.DIRECTORIES_ONLY) {
                        val file = value?.let { v -> File(v) }
                        SquaredIconButton(
                            icon = painterResource(Res.drawable.icon_folder_open_24px),
                            iconTint = iconTint,
                            size = 30.dp,
                            toolTip = stringResource(Res.string.tooltip_openInExplorer),
                            buttonShape = buttonShape,
                            buttonColor = buttonColor,
                            enabled = file?.exists() == true,
                            onClick = {
                                buttonScope.launch(Dispatchers.IO) {
                                    Desktop.getDesktop().open(file)
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showOkButton) {
            FlexibleTextButton(
                text = stringResource(Res.string.ok),
                height = 30.dp,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = onOkClick,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .align(Alignment.End),
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

    if (showDeleteDialog && onDeleteClick != null) {
        AlertDialog(
            modifier = Modifier
                .border(1.dp, focusedBorderColor),
            containerColor = Severity.Error.color().copy(alpha = 0.8f),
            shape = buttonShape,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(
                text = stringResource(Res.string.title_delete),
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
                    Icon(
                        painter = painterResource(Res.drawable.icon_warning_24px),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(100.dp)
                            .aspectRatio(1.0f)
                    )

                    Text(
                        text = stringResource(Res.string.warning_delete),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            },
            confirmButton = {
                FlexibleTextButton(
                    text = stringResource(Res.string.ok),
                    height = 30.dp,
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = onDeleteClick,
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
                        showDeleteDialog = false
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
