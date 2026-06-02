package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_arrow_forward_ios_24px
import de.visualdigits.compose.resources.icon_emergency_home_24px
import de.visualdigits.compose.resources.show_details_hint
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.domain.model.type.TextureType
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.style.gap
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProjectItem(
    settings: Settings?,
    project: ProjectConfiguration,
    onClick: () -> Unit,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val textureFormatPackage = project.get<TextureFormat>(PK.textureFormatPackage)
    val textureFormatModel = project.get<TextureFormat>(PK.textureFormatModel)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .border(
                width = 1.dp,
                color = if (isHovered) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.extraSmall
            )
            .hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
            .background(if (isHovered) Color.Black.copy(alpha = 0.4f) else Color.Transparent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // thumbnail
        Column(
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(project.thumbnailFile)
                        .size(Size.ORIGINAL)
                        .build(),
                    fallback = painterResource(Res.drawable.icon_emergency_home_24px)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(
                        ratio = 720.0f / 344.0f,
                        matchHeightConstraintsFirst = false
                    )
            )
        }

        // description
        Column(
            modifier = modifier
                .padding(horizontal = MaterialTheme.shapes.gap)
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "${project.get<String>(PK.airplaneName)} - ${project.get<String>(PK.liveryName)}",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = project.get<MutableList<TextureType>>(PK.textureTypes)?.joinToString(", ")?:"",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            val formatPackage = project.get<TextureFormat>(PK.textureFormatPackage)?.toString()
            val formatModel = project.get<TextureFormat>(PK.textureFormatModel)?.toString()
            Text(
                text = "$formatPackage <> $formatModel",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // conversion buttons
        ProjectButtons(
            modifier = modifier,
            settings = settings,
            textureFormatPackage = textureFormatPackage,
            project = project,
            onProjectListAction = onProjectListAction
        )

        // arrow icon
        Column(
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_arrow_forward_ios_24px),
                contentDescription = stringResource(Res.string.show_details_hint),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
