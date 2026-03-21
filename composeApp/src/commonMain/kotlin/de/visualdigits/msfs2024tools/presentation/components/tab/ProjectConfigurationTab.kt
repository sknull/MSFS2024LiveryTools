package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.presentation.components.ConfigurationEditForm
import de.visualdigits.common.presentation.components.ConfigurationPanel
import de.visualdigits.common.presentation.components.ErrorCard
import de.visualdigits.common.presentation.components.FlexibleTextButton
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.presentation.components.BusyPanel
import de.visualdigits.msfs2024tools.presentation.components.project.ProjectList
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
import msfs2024liverytools.composeapp.generated.resources.icon_create_new_folder_24px
import msfs2024liverytools.composeapp.generated.resources.new_project_hint
import msfs2024liverytools.composeapp.generated.resources.warning_no_results
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ProjectsTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    if (state.isLoading || state.isConverting) {
        BusyPanel(
            showTerminal = state.isConverting,
            state = state,
            onClick = {
                onProjectListAction(Msfs2024ToolsAction.OnBusyOkClick())
            }
        )
    } else if (state.projectConfigurations.isEmpty()) {
        ErrorCard(
            errorMessage = stringResource(Res.string.warning_no_results),
            severity = Severity.Warn,
            shapeContainer = ShapeContainer
        )
        Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth())
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
    ) {
        if (!state.isEditingProjectConfiguration && state.currentProjectConfiguration == null) {
            FlexibleTextButton(
                text = stringResource(Res.string.new_project_hint),
                height = 30.dp,
                paddingStart = 0.dp,
                paddingTop = 0.dp,
                onClick = {
                    onProjectListAction(
                        Msfs2024ToolsAction.OnNewProjectClick()
                    )
                },
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .align(Alignment.Start),
                buttonColor = ColorButton,
                buttonShape = ShapeButton,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_create_new_folder_24px),
                        contentDescription = null,
                        tint = ColorIcon
                    )
                }
            )
        }

        if (state.uiMessage != null) {
            ErrorCard(
                errorMessage = state.uiMessage.asString(),
                severity = state.uiMessageSeverity,
                shapeContainer = ShapeContainer
            )
            Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth())
        }

        when {
            state.currentProjectConfiguration != null -> {
                if (state.isEditingProjectConfiguration) {
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
                                Msfs2024ToolsAction.OnProjectConfigurationValueChanged(
                                    projectConfiguration = state.currentProjectConfiguration,
                                    keyValue = keyValue
                                ))
                        },
                        currentValue = { field ->
                            state.currentProjectConfiguration.get(field.descriptor.key as PK)
                        },
                        onCancelClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnEditProjectConfigurationCancelClick(
                                    originalProjectConfiguration = state.originalProjectConfiguration
                                )
                            )
                        },
                        onOkClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnSaveProjectConfigurationClick(
                                    projectConfiguration = state.currentProjectConfiguration
                                )
                            )
                        },
                        configuration = state.currentProjectConfiguration,
                        state = state
                    )
                } else {
                    ConfigurationPanel(
                        configuration = state.currentProjectConfiguration,
                        onEditClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnEditProjectConfigurationClick()
                            )
                        },
                        onDeleteClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnDeleteProjectClick(
                                    projectConfiguration = state.currentProjectConfiguration,
                                )
                            )
                        },
                        onOkClick = {
                            onProjectListAction(Msfs2024ToolsAction.OnPanelOkClick(state.settings))
                        },
                        iconTint = ColorIcon,
                        space = SpaceBetweenComponents,
                        showOkButton = true,
                        buttonColor = ColorButton,
                        buttonShape = ShapeButton,
                        focusedBorderColor = ColorFocused
                    )
                }
            }

            else -> {
                ProjectList(
                    onProjectListAction = onProjectListAction,
                    state = state
                )
            }
        }
    }
}
