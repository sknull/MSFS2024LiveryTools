package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.presentation.components.container.ErrorCard
import de.visualdigits.common.presentation.components.form.ConfigurationEditForm
import de.visualdigits.common.presentation.model.ScrollIntent
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.label_configuration
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.screen.ScreenFrame
import de.visualdigits.msfs2024tools.presentation.style.gap
import org.jetbrains.compose.resources.stringResource


@Composable
fun SettingsPage(
    state: Msfs2024ToolsState,
    modifier: Modifier = Modifier,
    platformType: PlatformType,
    onAction: (Msfs2024ToolsAction) -> Unit,
) {
    val scrollPosition= mutableMapOf<String, Triple<Int, Int?, ScrollIntent>>()

    ScreenFrame(
        modifier = modifier,
        containerModifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        contentModifier = Modifier
            .fillMaxSize(fraction = 0.97f)
            .clip(MaterialTheme.shapes.small)
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
                    .padding(MaterialTheme.shapes.gap)
                    .fillMaxSize()
            ) {

                if (state.uiMessage != null) {
                    ErrorCard(
                        errorMessage = state.uiMessage,
                        severity = state.uiMessageSeverity,
                        shapeContainer = MaterialTheme.shapes.small
                    )
                    Spacer(Modifier.height(MaterialTheme.shapes.gap).fillMaxWidth())
                }

                ConfigurationEditForm(
                    configuration = state.settings!!,
                    platformType = platformType,
                    scrollbarModifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .width(10.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                    scrollPosition = scrollPosition,
                    scrollbarId = "configuration_settings",
                    onValueChange = { keyValue ->
                        onAction(
                            Msfs2024ToolsAction.OnSettingsValueChanged(
                                settings = state.settings,
                                keyValue = keyValue
                            )
                        )
                    },
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
                    onCommonAction = { action ->
                    }
                )
            }
        }
    }
}
