package de.visualdigits.msfs2024tools

import androidx.compose.runtime.Composable
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import de.visualdigits.msfs2024tools.presentation.screen.MainScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    platformType: PlatformType
) {

    val viewModel = koinViewModel<Msfs2024ToolsViewModel>()

    MainScreen(
        viewModel = viewModel,
        platformType = platformType
    )
}
