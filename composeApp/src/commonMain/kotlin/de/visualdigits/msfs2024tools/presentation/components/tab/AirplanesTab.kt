package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.presentation.components.EditableList
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents

@Composable
fun AirplanesTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    EditableList(
        field = (state.settings?.fields["airplanes"] as? Field<ListFieldDescriptor<Any>, MutableList<Any>, Any>) ?: error("No settings"),
        fieldHeight = 70.dp,
        space = SpaceBetweenComponents,
        unfocusedBorderColor = ColorUnfocused,
        focusedBorderColor = ColorFocused,
        iconTint = ColorIcon,
        buttonColor = ColorButton,
        buttonShape = ShapeButton,
        containerShape = ShapeContainer,
        scrollable = true,
        onValueChange = { keyValue ->
            if (state.settings.get<List<String>>("airplanes") != keyValue.value?.split(",")) {
                val projectConfigurations = state.projectConfigurations
                projectConfigurations.filter { p ->
                    (p.get<List<String>>("airplaneName") ?: "") == (keyValue.previousValue ?: "")
                }.forEach { p ->
                    // prevent to rename unfinished edits
                    if (p.get<List<String>>("airplaneName") != null && keyValue.newValue != null) {
                        p.set("airplaneName", keyValue.newValue)
                    }
                }
                onProjectListAction(
                    Msfs2024ToolsAction.OnSaveAirplanesClick(
                        settings = state.settings.copy(key = "airplanes", value = keyValue.value),
                        projectConfigurations = projectConfigurations
                    )
                )
            }
        },
        deleteAllowed = { key, value ->
            when (key) {
                "airplanes" -> state.projectConfigurations.none { p ->
                    val airplaneName = p.get<String>("airplaneName") ?: ""
                    airplaneName == value
                }
                else -> true
            }
        }
    )
}
