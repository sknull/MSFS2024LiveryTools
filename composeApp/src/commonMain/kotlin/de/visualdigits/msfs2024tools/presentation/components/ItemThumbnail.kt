package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_emergency_home_24px
import org.jetbrains.compose.resources.painterResource


@Composable
fun RowScope.ItemThumbnail(
    modifier: Modifier,
    project: ProjectConfiguration
) {
    Column(
        modifier = modifier
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        var imageLoadResult by remember {
            mutableStateOf<Result<Painter>?>(null)
        }
        val painter = rememberAsyncImagePainter(
                model = project.thumbnailFile,
                onSuccess = { painter ->
                    imageLoadResult =
                        if (painter.painter.intrinsicSize.width > 1 && painter.painter.intrinsicSize.height > 1) {
                            Result.success(painter.painter)
                        } else {
                            Result.failure(IllegalStateException("Invalid image size"))
                        }
                },
                onError = { painter ->
                    painter.result.throwable.printStackTrace()
                    imageLoadResult = Result.failure(painter.result.throwable)
                }
            )

        when (val result = imageLoadResult) {
            null -> CircularProgressIndicator()
            else -> {
                Image(
                    painter = if (result.isSuccess) {
                        painter
                    } else {
                        painterResource(Res.drawable.icon_emergency_home_24px)
                    },
                    contentDescription = "Failed to load thumbnail for '${project.airplaneName}_${project.liveryName}'",
                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(
                            ratio = 720.0f / 344.0f,
                            matchHeightConstraintsFirst = false
                        )
                )
            }
        }
    }
}
