package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.form.EditableListStandalone
import de.visualdigits.common.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.common.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.common.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.common.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.common.presentation.style.ProjectStyle.SpaceBetweenComponents
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> AirplanesTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    val fieldDescriptor = state.settings?.lookupFieldDescriptors[SK.airplanes]!! as AbstractFieldDescriptor<Any, Any, K, FK, Any>
    val currentOption = fieldDescriptor.currentOption(state.settings as AbstractConfiguration<*, K>)
    val currentValue = state.settings.getUnsafe(fieldDescriptor.key)
    EditableListStandalone(
        fieldState = FieldState(
            configuration = state.settings,
            fieldDescriptor = fieldDescriptor,
            options = fieldDescriptor.options(state.settings, null),
            currentValue = currentValue,
            currentOption = currentOption,
            currentOptionUIText = currentOption?.second ?: UiText.DynamicString(
                currentOption?.first?.toString() ?: ""
            ),
            valid = fieldDescriptor.valid(state.settings, currentValue)
        ),
        fieldHeight = 70.dp,
        space = SpaceBetweenComponents,
        unfocusedBorderColor = ColorUnfocused,
        focusedBorderColor = ColorFocused,
        iconTint = ColorIcon,
        buttonShape = ShapeButton,
        containerShape = ShapeContainer,
        buttonColor = ColorButton,
        scrollable = true,
        onValueChange = { keyValue ->
            if (state.settings.get<List<String>>(SK.airplanes) != (keyValue.value as? String)?.split(",")) {
                val projectConfigurations = state.projectConfigurations
                val newConfigurations = projectConfigurations.filter { p ->
                    (p.get<List<String>>(PK.airplaneName) ?: "") == (keyValue.previousValue ?: "")
                }.mapNotNull { p ->                   // prevent to rename unfinished edits
                    if (p.get<List<String>>(PK.airplaneName) != null && keyValue.newValue != null) {
                        p.copy(PK.airplaneName, keyValue.newValue)
                    } else {
                        p
                    }
                }
                onProjectListAction(
                    Msfs2024ToolsAction.OnSaveAirplanesClick(
                        settings = state.settings.copy(key = SK.airplanes, value = keyValue.value),
                        projectConfigurations = newConfigurations
                    )
                )
            }
        }
    ) { descriptor, value ->
        when (descriptor.key) {
            SK.airplanes -> state.projectConfigurations.none { p ->
                val airplaneName = p.get<String>(PK.airplaneName) ?: ""
                airplaneName == value
            }

            else -> true
        }
    }
}
