package de.visualdigits.msfs2024tools.presentation.components.tab

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.presentation.components.container.ErrorCard
import de.visualdigits.common.presentation.components.button.FlexibleTextButton
import de.visualdigits.common.presentation.components.form.ConfigurationEditForm
import de.visualdigits.common.presentation.components.form.ConfigurationPanel
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.msfs2024tools.presentation.components.BusyPanel
import de.visualdigits.msfs2024tools.presentation.components.project.ProjectList
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.common.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.common.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.common.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.common.presentation.style.ProjectStyle.SpaceBetweenComponents
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_add_24px
import de.visualdigits.compose.resources.icon_cancel_24px
import de.visualdigits.compose.resources.icon_check_small_24px
import de.visualdigits.compose.resources.icon_create_new_folder_24px
import de.visualdigits.compose.resources.icon_delete_24px
import de.visualdigits.compose.resources.icon_edit_24px
import de.visualdigits.compose.resources.icon_file_save_24px
import de.visualdigits.compose.resources.icon_folder_open_24px
import de.visualdigits.compose.resources.new_project_hint
import de.visualdigits.compose.resources.warning_no_results
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ProjectsTab(
    state: Msfs2024ToolsState,
    onAction: (Msfs2024ToolsAction) -> Unit
) {
    val scrollPosition= mutableMapOf<String, Pair<Int, Int?>>()

    if (state.isLoading || state.isConverting) {
        BusyPanel(
            showTerminal = state.isConverting,
            state = state,
            onClick = {
                onAction(Msfs2024ToolsAction.OnBusyOkClick())
            }
        )
    } else if (state.projectConfigurations.isEmpty()) {
        ErrorCard(
            errorMessage = UiText.StringResourceId(Res.string.warning_no_results),
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
                    onAction(
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
                errorMessage = state.uiMessage,
                severity = state.uiMessageSeverity,
                shapeContainer = ShapeContainer
            )
            Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth())
        }

        when {
            state.currentProjectConfiguration != null -> {
                if (state.isEditingProjectConfiguration) {
                    ConfigurationEditForm(
                        configuration = state.currentProjectConfiguration,
                        scrollbarModifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .width(10.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        titleChooseDirectory = UiText.DynamicString("Choose Directory"),
                        titleChooseFile = UiText.DynamicString("Choose File"),
                        iconFolder = painterResource(Res.drawable.icon_folder_open_24px),
                        editableListResources = EditableListResources(
                            hintAdd = UiText.DynamicString("Add..."),
                            titleAdd = UiText.DynamicString("Add"),
                            iconAdd = painterResource(Res.drawable.icon_add_24px),
                            titleEdit = UiText.DynamicString("Edit"),
                            toolTipEdit = UiText.DynamicString("Edit"),
                            iconEdit = painterResource(Res.drawable.icon_edit_24px),
                            toolTipDelete = UiText.DynamicString("Delete"),
                            iconDelete = painterResource(Res.drawable.icon_delete_24px),
                            labelOk = UiText.DynamicString("Ok"),
                            iconOk = painterResource(Res.drawable.icon_check_small_24px),
                            labelCancel = UiText.DynamicString("Cancel"),
                            iconCancel = painterResource(Res.drawable.icon_cancel_24px),
                            iconSaveFile = painterResource(Res.drawable.icon_file_save_24px)
                        ),
                        tooltipOk = UiText.DynamicString(""),
                        iconOk = painterResource(Res.drawable.icon_check_small_24px),
                        tooltipCancel = UiText.DynamicString(""),
                        iconCancel = painterResource(Res.drawable.icon_cancel_24px),
                        scrollPosition = scrollPosition,
                        scrollbarId = "configuration_settings",
                        scrollbarStyle = PlatformScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = 8.dp,
                            shape = RoundedCornerShape(4.dp),
                            hoverDurationMillis = 300,
                            unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        fieldHeight = 50.dp,
                        onValueChange = { keyValue ->
                            onAction(
                                Msfs2024ToolsAction.OnProjectConfigurationValueChanged(
                                    projectConfiguration = state.currentProjectConfiguration,
                                    keyValue = keyValue
                                ))
                        },
                        onCancelClick = {
                            onAction(
                                Msfs2024ToolsAction.OnEditProjectConfigurationCancelClick(
                                    originalProjectConfiguration = state.originalProjectConfiguration
                                )
                            )
                        },
                        onOkClick = {
                            onAction(
                                Msfs2024ToolsAction.OnSaveProjectConfigurationClick(
                                    projectConfiguration = state.currentProjectConfiguration
                                )
                            )
                        },
                        onCommonAction = { action ->
                        }
                    )
                } else {
                    ConfigurationPanel(
                        configuration = state.currentProjectConfiguration,
                        onEditClick = {
                            onAction(
                                Msfs2024ToolsAction.OnEditProjectConfigurationClick()
                            )
                        },
                        onDeleteClick = {
                            onAction(
                                Msfs2024ToolsAction.OnDeleteProjectClick(
                                    projectConfiguration = state.currentProjectConfiguration,
                                )
                            )
                        },
                        onOkClick = {
                            onAction(Msfs2024ToolsAction.OnPanelOkClick(state.settings))
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
                    onAction = onAction,
                    state = state
                )
            }
        }
    }
}
