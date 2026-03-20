package de.visualdigits.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.color
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.`false`
import msfs2024liverytools.composeapp.generated.resources.`true`
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ComboBox(
    field: Field<*, *,*>,
    height: Dp = Dp.Unspecified,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    initialValue: String,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    buttonShape: Shape,
    options: List<Triple<String, StringResource?, DrawableResource?>>? = null,
    onValueChange: (KeyValue) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(initialValue)
    if (enabled) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            InnerTextField(
                field = field,
                modifier = modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .exposedDropdownSize(),
                height = height,
                buttonShape = buttonShape,
                textFieldState = textFieldState,
                expanded = expanded,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val options1 = field.descriptor.options()
                options?: options1.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(KeyValue(field.descriptor.key, option.first))
                            textFieldState.setTextAndPlaceCursorAtEnd(option.first)
                            expanded = false
                        },
                        leadingIcon = {
                            option.third?.let { icon ->
                                Image(
                                    painter = painterResource(icon),
                                    contentDescription = option.first,
                                    modifier = Modifier
                                        .height(30.dp)
                                )
                            }
                        },
                        text = {
                            option.second?.let { t ->
                                Text(text = stringResource(t))
                            }?:Text(text = option.first)
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            }
        }
    } else {
        InnerTextField(
            field = field,
            modifier = modifier,
            enabled = false,
            height = height,
            buttonShape = buttonShape,
            textFieldState = textFieldState,
            expanded = expanded,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedBorderColor = focusedBorderColor
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun InnerTextField(
    field: Field<*, *,*>,
    modifier: Modifier,
    enabled: Boolean = true,
    height: Dp = Dp.Unspecified,
    buttonShape: Shape,
    textFieldState: TextFieldState,
    expanded: Boolean,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color
) {
    ToolTip(field.descriptor.toolTip?.let { t -> stringResource(t) }) {
        OutlinedTextField(
            label = { Text(text = stringResource(field.descriptor.label)) },
            modifier = modifier
                .height(height),
            enabled = enabled,
            shape = buttonShape,
            readOnly = true,
            state = textFieldState,
            trailingIcon = if (enabled) {
                {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (field.valid()) unfocusedBorderColor else Severity.Error.color(),
                focusedBorderColor = focusedBorderColor,
                disabledTextColor = focusedBorderColor,
                disabledLabelColor = focusedBorderColor,
                disabledBorderColor = MaterialTheme.colorScheme.surfaceDim
            )
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BooleanComboBox(
    modifier: Modifier = Modifier,
    field: Field<*, *,*>,
    height: Dp = Dp.Unspecified,
    initialValue: String,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    buttonShape: Shape,
    onValueChange: (KeyValue) -> Unit,
) {
    ComboBox(
        modifier = modifier,
        field = field,
        height = height,
        options = listOf(
            Triple("true", Res.string.`true`, null),
            Triple("false", Res.string.`false`, null)
        ),
        initialValue = initialValue,
        unfocusedBorderColor = unfocusedBorderColor,
        focusedBorderColor = focusedBorderColor,
        buttonShape = buttonShape,
        onValueChange = onValueChange,
    )
}
