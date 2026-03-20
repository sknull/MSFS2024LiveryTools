package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.presentation.components.EditableList
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
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
        configuration = state.settings,
        field = (state.settings?.fields[SK.airplanes] as? Field<MutableList<*>, *, *>) ?: error("No settings"),
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
            if (state.settings.get<List<String>>(SK.airplanes) != keyValue.value?.split(",")) {
                val projectConfigurations = state.projectConfigurations
                projectConfigurations.filter { p ->
                    (p.get<List<String>>(PK.airplaneName) ?: "") == (keyValue.previousValue ?: "")
                }.forEach { p ->
                    // prevent to rename unfinished edits
                    if (p.get<List<String>>(PK.airplaneName) != null && keyValue.newValue != null) {
                        p.set(PK.airplaneName, keyValue.newValue)
                    }
                }
                onProjectListAction(
                    Msfs2024ToolsAction.OnSaveAirplanesClick(
                        settings = state.settings.copy(key = SK.airplanes, value = keyValue.value),
                        projectConfigurations = projectConfigurations
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
