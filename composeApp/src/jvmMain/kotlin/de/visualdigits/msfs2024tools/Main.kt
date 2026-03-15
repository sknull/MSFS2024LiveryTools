package de.visualdigits.msfs2024tools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import de.visualdigits.msfs2024tools.data.dto.configuration.GlobalConfigurationDto
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.presentation.components.Msfs2024InfoDialog
import de.visualdigits.msfs2024tools.presentation.components.Msfs2024MenuBar
import kotlinx.coroutines.cancel
import msfs2024liverytools.composeapp.generated.resources.Msfs2024Tools
import msfs2024liverytools.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
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

        val globalConfiguration = FilesystemConfigurationDataSource().loadConfiguration()
        val locale = globalConfiguration.language?.locale?: Locale.ENGLISH
        Locale.setDefault(locale)


        Window(
            onCloseRequest = {
                ioScope.cancel()
                exitApplication()
            },
            title = "MSFS 2024 Livery Tools",
            icon = painterResource(Res.drawable.Msfs2024Tools),
            state = state
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            var languageTrigger by remember { mutableStateOf(locale) }
            var showInfoDialog by remember { mutableStateOf(false) }

            key(languageTrigger) {
                MaterialTheme {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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

            Msfs2024MenuBar(
                scope = scope,
                showInfoDialog = { showInfoDialog = it },
                snackbarHostState = snackbarHostState,
                languageTrigger = { languageTrigger = it }
            )
        }
    }
}
