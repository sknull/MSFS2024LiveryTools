package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorCardBackground
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun ProjectListItem(
    globalConfiguration: GlobalConfiguration?,
    project: ProjectConfiguration,
    hazeState: HazeState,
    onClick: () -> Unit,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier =  modifier
            .clip(ShapeContainer)
            .fillMaxWidth()
            .background(ColorCardBackground)
            .hoverable(interactionSource = interactionSource)
            .clickable(onClick = onClick)
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    blurRadius = 30.dp,
                    backgroundColor = Color.Unspecified,
                    tint = HazeTint(Color.Black.copy(alpha = 0.6f))
                )
            )
    ) {
        Row(
            modifier = modifier
                .background(if (isHovered) ColorCardBackground else Color.Transparent)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            ItemThumbnail(modifier = modifier, project = project)
            ItemText(modifier = modifier, project = project)
            ItemButtons(
                modifier = modifier,
                globalConfiguration = globalConfiguration,
                project = project,
                onProjectListAction = onProjectListAction
            )
            ItemIcon(modifier = modifier)
        }
    }
}
