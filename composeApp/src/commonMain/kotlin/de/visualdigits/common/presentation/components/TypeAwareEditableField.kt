package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.color
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
    height: Dp = Dp.Unspecified,
    clazz: Class<out Any>,
    fileMode: FileMode? = null,
    startDirectory: File?,
    options: List<Pair<String, String>>,
    key: String,
    label: String,
    toolTip: String? = null,
    value: String?,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonShape: Shape,
    buttonColor: Color,
    enabled: Boolean = true,
    valid: () -> Boolean?,
    onValueChange: (KeyValue) -> Unit,
    hasFocus: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val finalUnfocusedBorderColor = if (valid() == false) {
        Severity.Error.color()
    } else if (value == null) {
        Severity.Warn.color()
    } else {
        unfocusedBorderColor
    }

    when {
        StringResourceEnumerable::class.java.isAssignableFrom(clazz)
                || Enumerable::class.java.isAssignableFrom(clazz) -> {
            ComboBox(
                modifier = modifier
                    .focusRequester(focusRequester),
                enabled = enabled,
                height = height,
                options = options,
                key = key,
                label = label,
                toolTip = toolTip,
                initialValue = value?:"",
                unfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                buttonShape = buttonShape,
                onValueChange = onValueChange,
                valid = valid
            )
        }

        Boolean::class.java == clazz -> {
            BooleanComboBox(
                modifier = modifier
                    .focusRequester(focusRequester),
                enabled = enabled,
                height = height,
                key = key,
                label = label,
                toolTip = toolTip,
                initialValue = value?:"",
                unfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                buttonShape = buttonShape,
                onValueChange = onValueChange,
                valid = valid
            )
        }

        File::class.java == clazz -> {
            val titleDirectories = stringResource((Res.string.choose_directory))
            val titleFiles = stringResource((Res.string.choose_file))

            ToolTip(toolTip) {
                OutlinedTextField(
                    modifier = modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .height(height),
                    enabled = enabled,
                    value = value?:"",
                    label = { Text(text = label) },
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        trailingIcon?.let { ti -> ti() }

                        if (enabled) {
                            SquaredIconButton(
                                icon = painterResource(Res.drawable.icon_folder_open_24px),
                                iconTint = iconTint,
                                size = 30.dp,
                                buttonShape = buttonShape,
                                buttonColor = buttonColor,
                                onClick = {
                                    scope.launch(Dispatchers.IO) {
                                        val chooser = JFileChooser().apply {
                                            if (fileMode == FileMode.FILES_ONLY) {
                                                val filter =
                                                    FileNameExtensionFilter(options.map { o -> o.first } .joinToString(", ") { o -> "*.$o" }, *options.map { o -> o.first }.toTypedArray())
                                                this.fileFilter = filter
                                                this.isAcceptAllFileFilterUsed = false
                                            }
                                            currentDirectory = value?.let { v -> File(v) }?:startDirectory
                                            fileSelectionMode = fileMode?.jFileChooserMode ?: JFileChooser.FILES_AND_DIRECTORIES
                                            dialogTitle = when (fileMode) {
                                                FileMode.DIRECTORIES_ONLY -> titleDirectories
                                                FileMode.FILES_ONLY -> titleFiles
                                                else -> titleFiles
                                            }
                                        }
                                        val result = chooser.showOpenDialog(null)
                                        if (result == JFileChooser.APPROVE_OPTION) {
                                            onValueChange(KeyValue(key, chooser.selectedFile.canonicalPath))
                                        }
                                    }
                                }
                            )
                        }
                    },
                    readOnly = true,
                    shape = buttonShape,
                    onValueChange = { value ->
                        onValueChange(KeyValue(key, value))
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
            ToolTip(toolTip) {
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
                        onValueChange(KeyValue(key, value))
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
