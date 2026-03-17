package de.visualdigits.msfs2024tools.presentation.components.project

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
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
import de.visualdigits.common.presentation.components.CollapsibleBox
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorFocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents


@Composable
fun ProjectList(
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
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
                .sortedBy { p -> p.airplaneName }
                .groupBy { p -> p.airplaneName }
                .map { (airplaneName, projects) ->
                    Pair(airplaneName, projects.sortedBy { p -> p.liveryName })
                }

            projects.forEach { (airplaneName, projects) ->
                CollapsibleBox(
                    title = airplaneName!!,
                    unfocusedBorderColor = ColorUnfocused,
                    focusedBorderColor = ColorFocused,
                    backgroundColor = Color.Black.copy(alpha = 0.2f),
                    buttonShape = ShapeButton,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeContainer),
                        verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
                    ) {
                        projects.forEach { project ->
                            ProjectItem(
                                settings = state.settings,
                                project = project,
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
