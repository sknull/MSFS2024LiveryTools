package de.visualdigits.msfs2024tools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.formdev.flatlaf.FlatDarculaLaf
import de.visualdigits.di.initKoin
import de.visualdigits.msfs2024tools.data.datasource.FilesystemConfigurationDataSource
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.presentation.components.Msfs2024InfoDialog
import de.visualdigits.msfs2024tools.presentation.components.Msfs2024MenuBar
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import kotlinx.coroutines.cancel
import msfs2024liverytools.composeapp.generated.resources.Msfs2024Tools
import msfs2024liverytools.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale
import javax.swing.UIManager

fun main() {

    initKoin()

    application {
        val ioScope = rememberCoroutineScope()
        val state = rememberWindowState(
//            width = 1200.dp,
//            height = 1024.dp,
            width = 1200.dp,
            height = 900.dp,
            position = WindowPosition(Alignment.Center)
        )

        UIManager.setLookAndFeel(FlatDarculaLaf())

        val settingsDto = FilesystemConfigurationDataSource().loadSettings()
        val language = settingsDto.language ?: Language.EN
        Locale.setDefault(language.locale)

        Window(
            onCloseRequest = {
                ioScope.cancel()
                exitApplication()
            },
            title = "MSFS 2024 Livery Tools",
            icon = painterResource(Res.drawable.Msfs2024Tools),
            state = state
        ) {
            val msfs2024ToolsViewModel = koinViewModel<Msfs2024ToolsViewModel>()

            var languageTrigger by remember { mutableStateOf(language) }
            var menuVisible by remember { mutableStateOf(true) }
            var showInfoDialog by remember { mutableStateOf(false) }

            if (menuVisible) {
                Msfs2024MenuBar(
                    scope = rememberCoroutineScope(),
                    msfs2024ToolsViewModel = msfs2024ToolsViewModel,
                    showInfoDialog = { showInfoDialog = it },
                    snackbarHostState = remember { SnackbarHostState() },
                    languageTrigger = {
                        languageTrigger = it
                        menuVisible = false
                    },
                    currentLanguage = languageTrigger
                )
            }

            LaunchedEffect(menuVisible) {
                if (!menuVisible) {
                    kotlinx.coroutines.yield()
                    menuVisible = true
                }
            }

            key(languageTrigger) {
                MaterialTheme {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = remember { SnackbarHostState() }) }
                    ) { padding ->
                        Box(
                            modifier = Modifier
                                .padding(padding)
                        ) {
                            App()
                        }
                    }

                    Msfs2024InfoDialog(
                        showInfoDialog = showInfoDialog,
                        setShowInfoDialog = { showInfoDialog = it },
                    )
                }
            }
        }
    }
}
