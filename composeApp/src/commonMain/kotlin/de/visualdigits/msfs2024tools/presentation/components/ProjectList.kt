package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents
import dev.chrisbanes.haze.HazeState


@Composable
fun ProjectList(
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
    state: Msfs2024ToolsState,
    hazeState: HazeState,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val listState = rememberLazyListState(0)

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
        ) {
            val projects = state.projectConfigurations
                .sortedBy { p -> p.airplaneName }
                .groupBy { p -> p.airplaneName }
                .map { (airplaneName, projects) ->
                    Pair(airplaneName, projects.sortedBy { p -> p.liveryName })
                }

            projects.forEach { (airplaneName, projects) ->
                item(key = "header_$airplaneName") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = ShapeContainer,
                        colors = CardColors(
                            containerColor = Color.Black.copy(0.2f),
                            contentColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = airplaneName?:"",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                items(
                    items = projects,
                    key = { project -> "body_${project.airplaneName}_${project.liveryName}" }
                ) { project ->
                    ProjectListItem(
                        settings = state.settings,
                        project = project,
                        hazeState = hazeState,
                        onClick = {
                            onProjectListAction(
                                Msfs2024ToolsAction.OnProjectClick(
                                    project
                                )
                            )
                        },
                        onProjectListAction = onProjectListAction,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                item(key = "spacer-footer_$airplaneName") { Spacer(Modifier.height(SpaceBetweenComponents).fillMaxWidth()) }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState = listState),
            interactionSource = interactionSource,
            modifier = Modifier
                .clip(ShapeContainer)
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .background(Color.Black.copy(alpha = 0.4f))
                .width(8.dp),
            style = defaultScrollbarStyle().copy(
                unhoverColor = Color.White.copy(alpha = 0.4f),
                hoverColor = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}
