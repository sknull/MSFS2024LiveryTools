package de.visualdigits.msfs2024tools.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.visualdigits.common.presentation.components.ImageBox
import de.visualdigits.common.presentation.components.TabButtonRow
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrows
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsScale
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsTranslationX
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundArrowsTranslationy
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.BackgroundImageMain
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonSelectedBgColor
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonSelectedFgColor
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonUnselectedBrush
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import de.visualdigits.msfs2024tools.presentation.components.tab.AirplanesTab
import de.visualdigits.msfs2024tools.presentation.components.tab.MsfsTabButton
import de.visualdigits.msfs2024tools.presentation.components.tab.GlobalConfigurationTab
import de.visualdigits.msfs2024tools.presentation.components.tab.ProjectsTab
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.tab_airplanes
import msfs2024liverytools.composeapp.generated.resources.tab_configuration
import msfs2024liverytools.composeapp.generated.resources.tab_projects
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectListScreenRoot(
    msfs2024ToolsViewModel: Msfs2024ToolsViewModel = koinViewModel()
) {
    ProjectListScreen(
        msfs2024ToolsViewModel = msfs2024ToolsViewModel
    )
}

@Composable
private fun ProjectListScreen(
    msfs2024ToolsViewModel: Msfs2024ToolsViewModel,
) {
    val state by msfs2024ToolsViewModel.state.collectAsStateWithLifecycle()

    val hazeState = rememberHazeState(blurEnabled = true)

    // blurred background image
    ImageBox(
        image = BackgroundImageMain,
        modifier = Modifier
            .hazeSource(state = hazeState, zIndex = 0f)
            .fillMaxSize()
    )

    // stacked non blurred overlay images and headline texts
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .hazeSource(state = hazeState, zIndex = 1f)
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    blurRadius = 30.dp,
                    backgroundColor = Color.Unspecified,
                    tint = HazeTint(Color.Black.copy(alpha = 0.2f))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(140.dp),
        ) {
            Image(
                painter = painterResource(BackgroundArrows),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Transparent)
                    .scale(BackgroundArrowsScale)
                    .graphicsLayer {
                        translationX = size.width * BackgroundArrowsTranslationX
                        translationY = size.height * BackgroundArrowsTranslationy
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 90.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "MSFS 2024 Livery Tools",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(0.dp)
                    )

                    Text(
                        text = state.selectedTabLabel?:"?",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(0.dp)
                    )
                }
            }
        }

        val firstTabLabel = stringResource(Res.string.tab_configuration)

        // menu tab buttons
        TabButtonRow(
            buttonRowModifier = Modifier
                .padding(start = 100.dp, bottom = 30.dp)
                .fillMaxWidth(),
            containerModifier = Modifier
                .fillMaxSize()
                .padding(start = 100.dp),
            contentModifier = Modifier
                .fillMaxSize(fraction = 0.97f)
                .clip(ShapeContainer)
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        blurRadius = 10.dp,
                        backgroundColor = Color.Unspecified,
                        tint = HazeTint(Color.Black.copy(alpha = 0.4f))
                    )
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            selectedTab = { state.selectedTabIndex },
            initializeViewModel = {
                if (state.selectedTabLabel == null) {
                    msfs2024ToolsViewModel.onAction(
                        Msfs2024ToolsAction.OnTabSelected(
                            0,
                            firstTabLabel
                        )
                    )
                }
                                  },
            items = linkedMapOf(
                stringResource(Res.string.tab_projects) to {
                    ProjectsTab(
                        state = state,
                        hazeState = hazeState,
                        onProjectListAction = { action ->
                            msfs2024ToolsViewModel.onAction(action)
                        }
                    )
                },
                stringResource(Res.string.tab_airplanes) to {
                    AirplanesTab(
                        state = state,
                        onProjectListAction = { action ->
                            msfs2024ToolsViewModel.onAction(action)
                        }
                    )
                },
                stringResource(Res.string.tab_configuration) to {
                    GlobalConfigurationTab(
                        state = state,
                        onProjectListAction = { action ->
                            msfs2024ToolsViewModel.onAction(action)
                        }
                    )
                },
            )
        ) { label, index ->
            MsfsTabButton(
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
                selectedFgColor = MsfsTabButtonSelectedFgColor,
                selectedBgColor = MsfsTabButtonSelectedBgColor,
                unselectedBrush = MsfsTabButtonUnselectedBrush,
                selected = state.selectedTabIndex == index,
                onClick = {
                    msfs2024ToolsViewModel.onAction(
                        Msfs2024ToolsAction.OnTabSelected(
                            index,
                            label
                        )
                    )
                },
                text = label,
            )
        }
    }
}
