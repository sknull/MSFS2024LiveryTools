package de.visualdigits.msfs2024tools

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.formdev.flatlaf.FlatDarculaLaf
import de.visualdigits.di.initKoin
import kotlinx.coroutines.cancel
import msfs2024liverytools.composeapp.generated.resources.Msfs2024Tools
import msfs2024liverytools.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import javax.swing.UIManager

fun main() {

    initKoin()

    application {
        val ioScope = rememberCoroutineScope()
        val state = rememberWindowState(
            width = 1200.dp,
            height = 900.dp,
            position = WindowPosition(Alignment.Center)
        )

        UIManager.setLookAndFeel(FlatDarculaLaf())

        Window(
            onCloseRequest = {
                ioScope.cancel()
                exitApplication()
            },
            title = "MSFS 2024 Livery Tools",
            icon = painterResource(Res.drawable.Msfs2024Tools),
            state = state
        ) {
            App()
        }
    }
}
