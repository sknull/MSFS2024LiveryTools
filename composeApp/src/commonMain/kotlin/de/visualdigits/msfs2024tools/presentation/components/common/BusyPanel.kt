package de.visualdigits.msfs2024tools.presentation.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.BusyProgressCircle
import de.visualdigits.common.presentation.components.FlexibleTextButton
import de.visualdigits.common.presentation.components.TerminalWindow
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.SpaceBetweenComponents
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.busy_hint
import msfs2024liverytools.composeapp.generated.resources.icon_check_small_24px
import msfs2024liverytools.composeapp.generated.resources.ok
import msfs2024liverytools.composeapp.generated.resources.stdout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun BusyPanel(
    state: Msfs2024ToolsState,
    showTerminal: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceBetweenComponents)
    ) {
        BusyProgressCircle(
            modifier = Modifier
                .fillMaxWidth(),
            label = stringResource(Res.string.busy_hint),
            progress = {
                state.currentProgress
            },
            color = ColorUnfocused,
            trackColor = Color(0xffcccccc),
            space = SpaceBetweenComponents
        )

        if (showTerminal) {
            TerminalWindow(
                modifier = Modifier
                    .weight(1f),
                shapeContainer = ShapeContainer,
                title = stringResource(Res.string.stdout),
                listData = {
                    state.logs
                }
            )
        }

        FlexibleTextButton(
            text = stringResource(Res.string.ok),
            height = 30.dp,
            paddingStart = 0.dp,
            paddingTop = 0.dp,
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.End)
                .pointerHoverIcon(PointerIcon.Hand),
            buttonColor = ColorButton,
            buttonShape = ShapeButton,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_check_small_24px),
                    contentDescription = null,
                    tint = ColorIcon
                )
            }
        )
    }
}
