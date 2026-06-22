package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.button.TabButtonRow
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.tab_airplanes
import de.visualdigits.compose.resources.tab_projects
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.screen.MsfsTabButton
import de.visualdigits.msfs2024tools.presentation.screen.ScreenFrame
import de.visualdigits.msfs2024tools.presentation.style.gap

@Composable
fun ProjectListPage(
    state: Msfs2024ToolsState,
    onAction: (Msfs2024ToolsAction) -> Unit
) {
    val items = linkedMapOf<Pair<String, UiText>, @Composable (() -> Unit)>(
        Pair("tab_projects", UiText.StringResourceId(Res.string.tab_projects)) to {
            ProjectsTab(
                state = state,
                onAction = { action ->
                    onAction(action)
                }
            )
        },
        Pair("tab_airplanes", UiText.StringResourceId(Res.string.tab_airplanes)) to {
            AirplanesTab(
                state = state,
                onProjectListAction = { action ->
                    onAction(action)
                }
            )
        }
    )

    ScreenFrame(
        modifier = Modifier,
        containerModifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        contentModifier = Modifier
            .fillMaxSize(fraction = 0.97f)
            .clip(MaterialTheme.shapes.small)
            .background(Color.Black.copy(alpha = 0.2f)),
        label = state.selectedTabLabel?.asString() ?: "?"
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.shapes.gap)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.shapes.gap)
        ) {
            // menu tab buttons
            TabButtonRow(
                modifier = Modifier
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
                    width = 160.dp,
                    height = 50.dp,
                    selected = state.selectedTabIndex == index,
                    onClick = {
                        onAction(
                            Msfs2024ToolsAction.OnTabSelected(index)
                        )
                    },
                    text = label.asString(),
                )
            }
        }
    }
}
