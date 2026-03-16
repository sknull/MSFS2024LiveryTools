package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuScope
import co.touchlab.kermit.Logger
import de.visualdigits.msfs2024tools.domain.model.configuration.Settings
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_info_24px
import msfs2024liverytools.composeapp.generated.resources.icon_language_24px
import msfs2024liverytools.composeapp.generated.resources.menu_about
import msfs2024liverytools.composeapp.generated.resources.menu_help
import msfs2024liverytools.composeapp.generated.resources.menu_language
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.util.Locale


@Composable
fun FrameWindowScope.Msfs2024MenuBar(
    scope: CoroutineScope,
    msfs2024ToolsViewModel: Msfs2024ToolsViewModel,
    showInfoDialog: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    languageTrigger: (Language) -> Unit,
    currentLanguage: Language
) {
    var helpText by remember { mutableStateOf("") }
    var aboutText by remember { mutableStateOf("") }
    var langText by remember { mutableStateOf("") }

    LaunchedEffect(currentLanguage) {
        helpText = getStringWithLocale(Res.string.menu_help, currentLanguage)
        aboutText = getStringWithLocale(Res.string.menu_about, currentLanguage)
        langText = getStringWithLocale(Res.string.menu_language, currentLanguage)
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

        Menu(
            text = langText,
        ) {
            Language.entries.forEach { language ->
                LanguageItem(
                    scope = scope,
                    msfs2024ToolsViewModel = msfs2024ToolsViewModel,
                    snackbarHostState = snackbarHostState,
                    languageTrigger = languageTrigger,
                    language = language,
                    label= language.resourceId
                )
            }
        }
    }
}

suspend fun getStringWithLocale(resource: StringResource, locale: Language): String {
    // Wir setzen die Default-Locale kurzzeitig fest, um den String zu laden,
    // falls die Library nicht direkt auf Parameter reagiert.
    // Da dies in einer Coroutine (suspend) läuft, blockiert es die UI nicht.
    return getString(resource)
}

@Composable
private fun MenuScope.LanguageItem(
    scope: CoroutineScope,
    msfs2024ToolsViewModel: Msfs2024ToolsViewModel,
    snackbarHostState: SnackbarHostState,
    language: Language,
    label: StringResource,
    languageTrigger: (Language) -> Unit
) {
    Item(
        text = stringResource(label),
        icon = painterResource(Res.drawable.icon_language_24px),
        onClick = {
            updateLanguage(
                scope,
                language,
                msfs2024ToolsViewModel.state.value.settings ?: error("No global configuration loaded"),
                msfs2024ToolsViewModel.configurationRepository,
                snackbarHostState
            ) {
                languageTrigger(it)
                msfs2024ToolsViewModel.onAction(Msfs2024ToolsAction.OnLanguageSelected(it))
            }
        }
    )
}

private fun updateLanguage(
    scope: CoroutineScope,
    language: Language,
    settings: Settings,
    configurationRepository: ConfigurationRepository,
    snackbarHostState: SnackbarHostState,
    onSuccess: (Language) -> Unit
) {
    scope.launch {
        try {
            Locale.setDefault(language.locale)
            settings.set("language", language.name)
            withContext((Dispatchers.IO)) {
                configurationRepository.saveSettings(settings)
            }
            onSuccess(language)
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(
                message = "Fehler beim Speichern der Konfiguration: ${e.localizedMessage}",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            Logger.e("ERROR: Save failed with message '${e.message}'", e)
        }
    }
}
