package de.visualdigits.msfs2024tools.presentation.components.project

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.container.VerticalCollapsibleBox
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.common.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.common.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.common.presentation.style.ProjectStyle.SpaceBetweenComponents
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_arrow_drop_down_24px
import de.visualdigits.compose.resources.icon_arrow_right_24px
import org.jetbrains.compose.resources.painterResource


@Composable
fun ProjectList(
    onAction: (Msfs2024ToolsAction) -> Unit,
    state: Msfs2024ToolsState
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scrollState = rememberScrollState(0)

        Column(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
        ) {
            val projects = state.projectConfigurations
                .sortedBy { p -> p.get<String>(PK.airplaneName) }
                .groupBy { p -> p.get<String>(PK.airplaneName) }
                .map { (airplaneName, projects) ->
                    Pair(airplaneName, projects.sortedBy { p -> p.get<String>(PK.liveryName) })
                }

            projects.forEach { (airplaneName, liveries) ->
                key(airplaneName) {
                    VerticalCollapsibleBox(
                        title = airplaneName,
                        unfocusedBorderColor = ColorUnfocused,
                        focusedBorderColor = ColorFocused,
                        backgroundColor = Color.Black.copy(alpha = 0.2f),
                        shape = ShapeButton,
                        isExpanded = projects.size == 1 || state.collapsibleState["airplane_$airplaneName"] == true,
                        iconArrowRight = painterResource(Res.drawable.icon_arrow_right_24px),
                        iconArrowDown = painterResource(Res.drawable.icon_arrow_drop_down_24px),
                        iconTint = Color.White,
                        onStateChange = { state->
                            onAction(Msfs2024ToolsAction.OnCollapsibleStateChange("airplane_$airplaneName", state))
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ShapeContainer),
                            verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
                        ) {
                            liveries.forEach { project ->
                                key(airplaneName, project) {
                                    ProjectItem(
                                        settings = state.settings,
                                        project = project,
                                        onClick = {
                                            onAction(
                                                Msfs2024ToolsAction.OnProjectClick(
                                                    project
                                                )
                                            )
                                        },
                                        onProjectListAction = onAction,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState = scrollState),
            interactionSource = interactionSource,
            modifier = Modifier
                .clip(ShapeContainer)
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .background(Color.Black.copy(alpha = 0.4f))
                .width(8.dp),
            style = defaultScrollbarStyle().copy(
                unhoverColor = Color.White.copy(alpha = 0.6f),
                hoverColor = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}
