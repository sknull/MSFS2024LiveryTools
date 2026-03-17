package de.visualdigits.msfs2024tools

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import de.visualdigits.msfs2024tools.presentation.screen.MainScreenRoot
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorSchemeMsfs2024Tools
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.TypographyMsfs2024Tools
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    val msfs2024ToolsViewModel = koinViewModel<Msfs2024ToolsViewModel>()

    MaterialTheme(
        typography = TypographyMsfs2024Tools,
        colorScheme = ColorSchemeMsfs2024Tools,
    ) {
        MainScreenRoot(
            msfs2024ToolsViewModel = msfs2024ToolsViewModel
        )
    }
}
