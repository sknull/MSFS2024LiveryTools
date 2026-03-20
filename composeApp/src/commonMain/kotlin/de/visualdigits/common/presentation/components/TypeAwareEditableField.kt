package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.color
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.ReferenceListFieldDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.choose_directory
import msfs2024liverytools.composeapp.generated.resources.choose_file
import msfs2024liverytools.composeapp.generated.resources.icon_folder_open_24px
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun TypeAwareEditableField(
    modifier: Modifier = Modifier,
    configuration: AbstractConfiguration<*,*>?,
    field: Field<*,*,*>,
    currentValue: String? = null,
    height: Dp = Dp.Unspecified,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonShape: Shape,
    buttonColor: Color,
    enabled: Boolean = true,
    onValueChange: (KeyValue) -> Unit,
    hasFocus: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val value = currentValue?:field.stringValue()
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val valid = field.valid(field.value)
    val finalUnfocusedBorderColor = if (!valid) {
        Severity.Error.color()
    } else if (value == null) {
        Severity.Warn.color()
    } else {
        unfocusedBorderColor
    }

    when {
        field.descriptor is EnumFieldDescriptor
                || field.descriptor is ReferenceListFieldDescriptor
                || field.descriptor.itemClass?.java?.let { fc -> Enumerable::class.java.isAssignableFrom(fc) } == true -> {
            if (field.descriptor.fieldClass == Boolean::class) {
                BooleanComboBox(
                    modifier = modifier
                        .focusRequester(focusRequester),
                    field = field,
                    initialValue = value?:"",
                    height = height,
                    unfocusedBorderColor = finalUnfocusedBorderColor,
                    focusedBorderColor = focusedBorderColor,
                    buttonShape = buttonShape,
                    onValueChange = onValueChange,
                )
            } else {
                ComboBox(
                    modifier = modifier
                        .focusRequester(focusRequester),
                    field = field,
                    enabled = enabled,
                    height = height,
                    initialValue = value?:"",
                    unfocusedBorderColor = finalUnfocusedBorderColor,
                    focusedBorderColor = focusedBorderColor,
                    buttonShape = buttonShape,
                    onValueChange = onValueChange,
                )
            }
        }

        field.descriptor is FileFieldDescriptor -> {
            val titleDirectories = stringResource((Res.string.choose_directory))
            val titleFiles = stringResource((Res.string.choose_file))

            ToolTip(field.descriptor.toolTip?.let { t -> stringResource(t) }) {
                OutlinedTextField(
                    modifier = modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .height(height),
                    enabled = enabled,
                    value = value?:"",
                    label = { Text(text = stringResource(field.descriptor.label)) },
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        trailingIcon?.let { ti -> ti() }

                        if (enabled) {
                            SquaredIconButton(
                                icon = painterResource(Res.drawable.icon_folder_open_24px),
                                iconTint = iconTint,
                                modifier = Modifier.padding(start = 5.dp),
                                size = 30.dp,
                                buttonShape = buttonShape,
                                buttonColor = buttonColor,
                                onClick = {
                                    scope.launch(Dispatchers.IO) {
                                        val chooser = JFileChooser().apply {
                                            if (field.descriptor.fileMode == FileMode.FILES_ONLY) {
                                                val filter =
                                                    FileNameExtensionFilter(
                                                        field.descriptor.options().map { o -> o.first }.joinToString(", ") { o -> "*.$o" },
                                                        *field.descriptor.options().map { o -> o.first }.toTypedArray()
                                                    )
                                                this.fileFilter = filter
                                                this.isAcceptAllFileFilterUsed = false
                                            }
                                            currentDirectory = (field.value as? File)?:field.descriptor.startDirectory(configuration)
                                            fileSelectionMode = field.descriptor.fileMode.jFileChooserMode
                                            dialogTitle = when (field.descriptor.fileMode) {
                                                FileMode.DIRECTORIES_ONLY -> titleDirectories
                                                FileMode.FILES_ONLY -> titleFiles
                                            }
                                        }
                                        val result = chooser.showOpenDialog(null)
                                        if (result == JFileChooser.APPROVE_OPTION) {
                                            onValueChange(KeyValue(field.descriptor, chooser.selectedFile.canonicalPath))
                                        }
                                    }
                                }
                            )
                        }
                    },
                    shape = buttonShape,
                    onValueChange = { value ->
                        onValueChange(KeyValue(field.descriptor, value))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = finalUnfocusedBorderColor,
                        focusedBorderColor = focusedBorderColor
                    )
                )
            }
        }

        else -> {
            val label = stringResource(field.descriptor.label)
            ToolTip(field.descriptor.toolTip?.let { t -> stringResource(t) }) {
                OutlinedTextField(
                    modifier = modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .height(height),
                    enabled = enabled,
                    label = { Text(text = label) },
                    value = value?:"",
                    shape = buttonShape,
                    onValueChange = { value ->
                        onValueChange(KeyValue(field.descriptor, value))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = finalUnfocusedBorderColor,
                        focusedBorderColor = focusedBorderColor
                    )
                )
            }
        }
    }

    if (hasFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
