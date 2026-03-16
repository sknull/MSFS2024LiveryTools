package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.label_airplanes
import org.jetbrains.compose.resources.stringResource

@Composable
fun AirplanesTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        EditableList(
            height = 70.dp,
            space = SpaceBetweenComponents,
            unfocusedBorderColor = ColorUnfocused,
            focusedBorderColor = ColorFocused,
            iconTint = ColorIcon,
            buttonColor = ColorButton,
            buttonShape = ShapeButton,
            containerShape = ShapeContainer,
            key = "airplaneName",
            label = stringResource(Res.string.label_airplanes),
            clazz = String::class.java,
            options = state.settings?.airplanes?.map { a -> Pair(a, a) }?:listOf(),
            values = state.settings?.airplanes?:listOf(),
            onValueChange = { keyValue ->
                if (state.settings?.airplanes != keyValue.value?.split(",")?.toMutableList()) {
                    val projectConfigurations = state.projectConfigurations
                    projectConfigurations.filter { p ->
                        p.airplaneName == keyValue.previousValue
                    }.forEach { p ->
                        // prevent to rename unfinished edits
                        if (p.airplaneName != null && keyValue.newValue != null) {
                            p.airplaneName = keyValue.newValue
                        }
                    }
                    onProjectListAction(
                        Msfs2024ToolsAction.OnSaveAirplanesClick(
                            settings = state.settings?.copy(key = "airplanes", value = keyValue.value),
                            projectConfigurations = projectConfigurations
                        )
                    )
                }
            },
            deleteAllowed = { key, value ->
                when (key) {
                    "airplaneName" -> state.projectConfigurations.none { p -> p.airplaneName == value }
                    else -> true
                }
            }
        )
    }
}
