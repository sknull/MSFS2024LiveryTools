package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.button.FlexibleTextButton
import de.visualdigits.common.presentation.components.container.TerminalWindow
import de.visualdigits.common.presentation.style.ProjectStyle.ColorButton
import de.visualdigits.common.presentation.style.ProjectStyle.ColorIcon
import de.visualdigits.common.presentation.style.ProjectStyle.ColorUnfocused
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.common.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.common.presentation.style.ProjectStyle.SpaceBetweenComponents
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.busy_hint
import de.visualdigits.compose.resources.icon_check_small_24px
import de.visualdigits.compose.resources.ok
import de.visualdigits.compose.resources.stdout
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.busy_hint),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(SpaceBetweenComponents))

            CircularProgressIndicator(
                progress = {
                    state.currentProgress
                },
                color = ColorUnfocused,
                trackColor = Color(0xffcccccc)
            )
        }

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
