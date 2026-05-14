package de.visualdigits.msfs2024tools.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_info_24px
import de.visualdigits.compose.resources.icon_menu_24px
import de.visualdigits.compose.resources.icon_settings_24px
import de.visualdigits.compose.resources.label_configuration
import de.visualdigits.compose.resources.title_info
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import de.visualdigits.msfs2024tools.presentation.screen.page.InfoPage
import de.visualdigits.msfs2024tools.presentation.screen.page.ProjectListPage
import de.visualdigits.msfs2024tools.presentation.screen.page.SettingsPage
import de.visualdigits.msfs2024tools.presentation.style.DisplayThemeEnum
import de.visualdigits.msfs2024tools.presentation.style.MyShapes
import de.visualdigits.msfs2024tools.presentation.style.typography
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    viewModel: Msfs2024ToolsViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val displayTheme = DisplayThemeEnum.DARK


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val screenWidth = maxWidth

        val sizeFactor = when {
            screenWidth < 500.dp -> 0.9f
            screenWidth > 1500.dp -> 1.5f
            else -> 1.0f
        }

        MaterialTheme(
            colorScheme = displayTheme.colorScheme,
            typography = typography(
                textColor = displayTheme.textColor,
                sizeFactor = sizeFactor
            ),
            shapes = MyShapes
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF071323))
                        .width(40.dp)
                        .fillMaxSize()
                        .drawBehind() {
                            val strokeWidth = 1.dp.toPx()
                            drawLine(
                                color = Color(0xFF618CC1),
                                start = Offset(size.width - strokeWidth / 2, 0f),
                                end = Offset(size.width - strokeWidth / 2, size.height),
                                strokeWidth = strokeWidth
                            )
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        IndicatorButton(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            leadingIcon = painterResource(Res.drawable.icon_menu_24px),
                            leadingIconTint = Color.White,
                            width = 30.dp,
                            height = 30.dp,
                            onClick = {
                                viewModel.onAction(Msfs2024ToolsAction.OnTabSelected(0))
                            }
                        )

                        IndicatorButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 30.dp),
                            leadingIcon = painterResource(Res.drawable.icon_settings_24px),
                            leadingIconTint = Color.White,
                            toolTip = stringResource(Res.string.label_configuration),
                            width = 30.dp,
                            height = 30.dp,
                            onClick = {
                                viewModel.onAction(Msfs2024ToolsAction.OnEditSettingsClick(!state.isEditingSettings))
                            }
                        )

                        IndicatorButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            leadingIcon = painterResource(Res.drawable.icon_info_24px),
                            leadingIconTint = Color.White,
                            toolTip = stringResource(Res.string.title_info),
                            width = 30.dp,
                            height = 30.dp,
                            onClick = {
                                viewModel.onAction(Msfs2024ToolsAction.OnShowInfosClick(!state.isShowInfos))
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    // background image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .paint(
                                painter = BitmapPainter(imageResource(displayTheme.backgroundImage)),
                                alignment = Alignment.TopStart,
                                contentScale = ContentScale.FillBounds
                            )
                    )

                    when {
                        state.isEditingSettings -> SettingsPage(
                            state = state,
                            onAction = { action ->
                                viewModel.onAction(action)
                            }
                        )
                        state.isShowInfos -> InfoPage(
                        )
                        else -> ProjectListPage(
                            state = state,
                            onAction = { action ->
                                viewModel.onAction(action)
                            }
                        )
                    }
                }
            }
        }
    }
}
