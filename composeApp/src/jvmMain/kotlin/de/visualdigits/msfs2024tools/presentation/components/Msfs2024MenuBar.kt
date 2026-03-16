package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import de.visualdigits.msfs2024tools.domain.model.type.Language
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_info_24px
import msfs2024liverytools.composeapp.generated.resources.menu_about
import msfs2024liverytools.composeapp.generated.resources.menu_help
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource


@Composable
fun FrameWindowScope.Msfs2024MenuBar(
    showInfoDialog: (Boolean) -> Unit,
    currentLanguage: Language
) {
    var helpText by remember { mutableStateOf("") }
    var aboutText by remember { mutableStateOf("") }

    LaunchedEffect(currentLanguage) {
        helpText = getStringWithLocale(Res.string.menu_help, currentLanguage)
        aboutText = getStringWithLocale(Res.string.menu_about, currentLanguage)
    }

    MenuBar {
        Menu(
            text = helpText,
        ) {
            Item(
                text = aboutText,
                icon = painterResource(Res.drawable.icon_info_24px),
                onClick = {
                    showInfoDialog(true)
                }
            )
        }
    }
}

suspend fun getStringWithLocale(resource: StringResource, locale: Language): String {
    // Wir setzen die Default-Locale kurzzeitig fest, um den String zu laden,
    // falls die Library nicht direkt auf Parameter reagiert.
    // Da dies in einer Coroutine (suspend) läuft, blockiert es die UI nicht.
    return getString(resource)
}
