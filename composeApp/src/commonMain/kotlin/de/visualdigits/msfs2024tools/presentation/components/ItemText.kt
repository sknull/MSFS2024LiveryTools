package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.PaddingContainer


@Composable
fun RowScope.ItemText(
    modifier: Modifier,
    project: ProjectConfiguration
) {
    Column(
        modifier = modifier
            .padding(horizontal = PaddingContainer)
            .weight(1f),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "${project.airplaneName} - ${project.liveryName}",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = project.textureTypes.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = project.textureFormat?.toString() ?: "",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
