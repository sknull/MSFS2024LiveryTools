package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.ConfigurationEditForm
import de.visualdigits.common.presentation.components.ConfigurationPanel
import de.visualdigits.common.presentation.components.ErrorCard
import de.visualdigits.msfs2024tools.presentation.components.common.BusyPanel
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.PaddingContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents


@Composable
fun GlobalConfigurationTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
) {
    if (state.isLoading) {
        BusyPanel(
            state = state,
            onClick = {
                onProjectListAction(Msfs2024ToolsAction.OnBusyOkClick())
            }
        )
    } else {
        Column(
            modifier = Modifier
                .padding(PaddingContainer)
                .fillMaxSize()
        ) {

            if (state.errorMessage != null) {
                ErrorCard(
                    errorMessage = state.errorMessage.asString(),
                    shapeContainer = ShapeContainer
                )
                Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth())
            }

            if (state.isEditingGlobalConfiguration) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
                ) {
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
                            onProjectListAction(
                                Msfs2024ToolsAction.OnGlobalConfigurationValueChanged(
                                    globalConfiguration = state.globalConfiguration,
                                    keyValue = keyValue
                                )
                            )
                        },
                        onCancelClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnEditGlobalConfigurationCancelClick()
                            )
                        },
                        onOkClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnSaveGlobalConfigurationClick(
                                    globalConfiguration = state.globalConfiguration,
                                    projectConfigurations = state.projectConfigurations
                                )
                            )
                        },
                        configuration = state.globalConfiguration,
                        state = state
                    )
                }
            } else {
                ConfigurationPanel(
                    configuration = state.globalConfiguration,
                    onEditClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnEditGlobalConfigurationClick()
                        )
                    },
                    onOkClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnPanelOkClick(
                                configuration = state.globalConfiguration
                            )
                        )
                    },
                    iconTint = ColorIcon,
                    space = SpaceBetweenComponents,
                    buttonColor = ColorButton,
                    buttonShape = ShapeButton,
                    focusedBorderColor = ColorFocused
                )
            }
        }
    }
}
