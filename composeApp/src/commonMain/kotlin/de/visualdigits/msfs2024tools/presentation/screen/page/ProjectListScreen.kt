package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.TabButtonRow
import de.visualdigits.msfs2024tools.presentation.components.msfsbutton.MsfsTabButton
import de.visualdigits.msfs2024tools.presentation.components.tab.AirplanesTab
import de.visualdigits.msfs2024tools.presentation.components.tab.ProjectsTab
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.screen.ScreenFrame
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.PaddingContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.tab_airplanes
import msfs2024liverytools.composeapp.generated.resources.tab_projects
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProjectListScreen(
    state: Msfs2024ToolsState,
    modifier: Modifier = Modifier,
    onAction: (Msfs2024ToolsAction) -> Unit
) {
    ScreenFrame(
        modifier = modifier,
        containerModifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        contentModifier = Modifier
            .fillMaxSize(fraction = 0.97f)
            .clip(ShapeContainer)
            .background(Color.Black.copy(alpha = 0.2f)),
        label = state.selectedTabLabel?.let { l -> stringResource(l) } ?: "?"
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
        ) {
            // menu tab buttons
            val items = linkedMapOf<StringResource, @Composable (() -> Unit)>(
                Res.string.tab_projects to {
                    ProjectsTab(
                        state = state,
                        onProjectListAction = { action ->
                            onAction(action)
                        }
                    )
                },
                Res.string.tab_airplanes to {
                    AirplanesTab(
                        state = state,
                        onProjectListAction = { action ->
                            onAction(action)
                        }
                    )
                }
            )
            TabButtonRow(
                buttonRowModifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                selectedTab = { state.selectedTabIndex },
                initializeViewModel = {
                    if (state.selectedTabLabel == null) {
                        onAction(
                            Msfs2024ToolsAction.OnInitializeTabs(
                                tabLabels = items.keys.toList()
                            )
                        )
                    }
                },
                items = items
            ) { label, index ->
                MsfsTabButton(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    selected = state.selectedTabIndex == index,
                    onClick = {
                        onAction(
                            Msfs2024ToolsAction.OnTabSelected(index)
                        )
                    },
                    text = stringResource(label),
                )
            }
        }
    }
}
