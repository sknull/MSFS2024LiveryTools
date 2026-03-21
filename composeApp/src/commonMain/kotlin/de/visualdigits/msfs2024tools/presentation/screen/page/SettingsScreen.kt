package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.ConfigurationEditForm
import de.visualdigits.common.presentation.components.ErrorCard
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.presentation.components.BusyPanel
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.screen.ScreenFrame
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.PaddingContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.label_configuration
import org.jetbrains.compose.resources.stringResource


@Composable
fun SettingsScreen(
    state: Msfs2024ToolsState,
    modifier: Modifier = Modifier,
    onAction: (Msfs2024ToolsAction) -> Unit,
) {
    ScreenFrame(
        modifier = modifier,
        containerModifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        contentModifier = Modifier
            .fillMaxSize(fraction = 0.97f)
            .clip(ShapeContainer)
            .background(Color.Black.copy(alpha = 0.4f)),
        label = stringResource(Res.string.label_configuration)
    ) {
        if (state.isLoading) {
            BusyPanel(
                state = state,
                onClick = {
                    onAction(Msfs2024ToolsAction.OnBusyOkClick())
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(PaddingContainer)
                    .fillMaxSize()
            ) {

                if (state.uiMessage != null) {
                    ErrorCard(
                        errorMessage = state.uiMessage.asString(),
                        severity = state.uiMessageSeverity,
                        shapeContainer = ShapeContainer
                    )
                    Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth())
                }

                ConfigurationEditForm(
                    fieldHeight = 70.dp,
                    unfocusedBorderColor = ColorUnfocused,
                    focusedBorderColor = ColorFocused,
                    iconTint = ColorIcon,
                    buttonShape = ShapeButton,
                    buttonColor = ColorButton,
                    containerShape = ShapeContainer,
                    space = SpaceBetweenComponents,
                    onValueChange = { keyValue ->
                        onAction(
                            Msfs2024ToolsAction.OnSettingsValueChanged(
                                settings = state.settings,
                                keyValue = keyValue
                            )
                        )
                    },
                    currentValue = { field -> state.settings?.get(field.descriptor.key as SK) },
                    onCancelClick = {
                        onAction(
                            Msfs2024ToolsAction.OnEditSettingsCancelClick()
                        )
                    },
                    onOkClick = {
                        onAction(
                            Msfs2024ToolsAction.OnSaveSettingsClick(
                                settings = state.settings,
                                projectConfigurations = state.projectConfigurations
                            )
                        )
                    },
                    configuration = state.settings,
                    state = state
                )
            }
        }
    }
}
