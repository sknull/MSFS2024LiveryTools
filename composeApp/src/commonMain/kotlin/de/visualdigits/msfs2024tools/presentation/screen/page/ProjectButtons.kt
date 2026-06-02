package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.conversion_dds_to_png
import de.visualdigits.compose.resources.conversion_ktx2_to_png
import de.visualdigits.compose.resources.conversion_png_to_dds
import de.visualdigits.compose.resources.conversion_png_to_ktx2
import de.visualdigits.compose.resources.icon_create2d_24px
import de.visualdigits.compose.resources.label_dryrun
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.style.gap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun RowScope.ProjectButtons(
    modifier: Modifier,
    settings: Settings?,
    textureFormatPackage: TextureFormat?,
    project: ProjectConfiguration,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    var isCheckedDryRun by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .padding(horizontal = MaterialTheme.shapes.gap)
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.shapes.gap),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.label_dryrun),
                style = MaterialTheme.typography.bodySmall
            )
            Checkbox(
                checked = isCheckedDryRun,
                onCheckedChange = {
                    isCheckedDryRun = it
                }
            )
        }

        when {
//            textureFormatPackage == TextureFormat.KTX2 && textureFormatModel == TextureFormat.DDS
//            || textureFormatPackage == TextureFormat.DDS && textureFormatModel == TextureFormat.KTX2 -> {
//                ConversionButton(
//                    label = Res.string.conversion_ktx2_to_dds,
//                    icon = Res.drawable.icon_create2d_24px,
//                    conversion = null,
//                    onProjectListAction = onProjectListAction,
//                    settings = settings,
//                    project = project,
//                    isCheckedDryRun = isCheckedDryRun
//                )
//                ConversionButton(
//                    label = Res.string.conversion_dds_to_ktx2,
//                    icon = Res.drawable.icon_create2d_24px,
//                    conversion = null,
//                    onProjectListAction = onProjectListAction,
//                    settings = settings,
//                    project = project,
//                    isCheckedDryRun = isCheckedDryRun
//                )
//            }
            textureFormatPackage == TextureFormat.DDS -> {
                ConversionButton(
                    label = Res.string.conversion_png_to_dds,
                    icon = Res.drawable.icon_create2d_24px,
                    leadingIcon = true,
                    conversion = Conversion.PNG_TO_DDS,
                    onProjectListAction = onProjectListAction,
                    settings = settings,
                    project = project,
                    isCheckedDryRun = isCheckedDryRun
                )
                ConversionButton(
                    label = Res.string.conversion_dds_to_png,
                    icon = Res.drawable.icon_create2d_24px,
                    leadingIcon = false,
                    conversion = Conversion.DDS_TO_PNG,
                    onProjectListAction = onProjectListAction,
                    settings = settings,
                    project = project,
                    isCheckedDryRun = isCheckedDryRun
                )
            }
            textureFormatPackage == TextureFormat.KTX2 -> {
                ConversionButton(
                    label = Res.string.conversion_png_to_ktx2,
                    icon = Res.drawable.icon_create2d_24px,
                    leadingIcon = true,
                    conversion = Conversion.PNG_TO_KTX2,
                    onProjectListAction = onProjectListAction,
                    settings = settings,
                    project = project,
                    isCheckedDryRun = isCheckedDryRun
                )
                ConversionButton(
                    label = Res.string.conversion_ktx2_to_png,
                    icon = Res.drawable.icon_create2d_24px,
                    leadingIcon = false,
                    conversion = Conversion.KTX2_TO_PNG,
                    onProjectListAction = onProjectListAction,
                    settings = settings,
                    project = project,
                    isCheckedDryRun = isCheckedDryRun
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.ConversionButton(
    label: StringResource,
    icon: DrawableResource,
    leadingIcon: Boolean? = null,
    conversion: Conversion?,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit,
    settings: Settings?,
    project: ProjectConfiguration,
    isCheckedDryRun: Boolean
) {
    IndicatorButton(
        text = stringResource(label),
        onClick = {
            onProjectListAction(
                Msfs2024ToolsAction.OnConversionClick(
                    settings = settings,
                    currentProjectConfiguration = project,
                    conversion = conversion,
                    dryRun = isCheckedDryRun
                )
            )
        },
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .align(Alignment.Start),
        buttonColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = MaterialTheme.shapes.extraSmall,
        leadingIcon = if (leadingIcon == true) painterResource(icon) else null,
        trailingIcon = if (leadingIcon == false) painterResource(icon) else null
    )
}
