package de.visualdigits.msfs2024tools.presentation.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import de.visualdigits.common.presentation.components.FlexibleTextButton
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.configuration.ProjectConfiguration
import de.visualdigits.msfs2024tools.domain.model.type.Conversion
import de.visualdigits.msfs2024tools.domain.model.type.TextureFormat
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.PaddingContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.conversion_dds_to_png
import msfs2024liverytools.composeapp.generated.resources.conversion_ktx2_to_png
import msfs2024liverytools.composeapp.generated.resources.conversion_png_to_dds
import msfs2024liverytools.composeapp.generated.resources.conversion_png_to_ktx2
import msfs2024liverytools.composeapp.generated.resources.icon_create2d_24px
import msfs2024liverytools.composeapp.generated.resources.label_dryrun
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun RowScope.ItemButtons(
    modifier: Modifier,
    globalConfiguration: GlobalConfiguration?,
    project: ProjectConfiguration,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    var isCheckedDryRun by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .padding(horizontal = PaddingContainer)
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents),
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
        when (project.textureFormat) {
            TextureFormat.DDS -> {
                FlexibleTextButton(
                    text = stringResource(Res.string.conversion_png_to_dds),
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnConversionClick(
                                globalConfiguration = globalConfiguration,
                                currentProjectConfiguration = project,
                                conversion = Conversion.PNG_TO_DDS,
                                dryRun = isCheckedDryRun
                            )
                        )
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .align(Alignment.Start),
                    buttonColor = ColorButton,
                    buttonShape = ShapeButton,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_create2d_24px),
                            contentDescription = null,
                            tint = ColorIcon
                        )
                    }
                )
                FlexibleTextButton(
                    text = stringResource(Res.string.conversion_dds_to_png),
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnConversionClick(
                                globalConfiguration = globalConfiguration,
                                currentProjectConfiguration = project,
                                conversion = Conversion.DDS_TO_PNG,
                                dryRun = isCheckedDryRun
                            )
                        )
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .align(Alignment.Start),
                    buttonColor = ColorButton,
                    buttonShape = ShapeButton,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_create2d_24px),
                            contentDescription = null,
                            tint = ColorIcon
                        )
                    }
                )
            }

            TextureFormat.KTX2 -> {
                FlexibleTextButton(
                    text = stringResource(Res.string.conversion_png_to_ktx2),
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnConversionClick(
                                globalConfiguration = globalConfiguration,
                                currentProjectConfiguration = project,
                                conversion = Conversion.PNG_TO_KTX2,
                                dryRun = isCheckedDryRun
                            )
                        )
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .align(Alignment.Start),
                    buttonColor = ColorButton,
                    buttonShape = ShapeButton,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_create2d_24px),
                            contentDescription = null,
                            tint = ColorIcon
                        )
                    }
                )
                FlexibleTextButton(
                    text = stringResource(Res.string.conversion_ktx2_to_png),
                    paddingStart = 0.dp,
                    paddingTop = 0.dp,
                    onClick = {
                        onProjectListAction(
                            Msfs2024ToolsAction.OnConversionClick(
                                globalConfiguration = globalConfiguration,
                                currentProjectConfiguration = project,
                                conversion = Conversion.KTX2_TO_PNG,
                                dryRun = isCheckedDryRun
                            )
                        )
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .align(Alignment.Start),
                    buttonColor = ColorButton,
                    buttonShape = ShapeButton,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_create2d_24px),
                            contentDescription = null,
                            tint = ColorIcon
                        )
                    }
                )
            }

            else -> {
                // should not happen
            }
        }
    }
}
